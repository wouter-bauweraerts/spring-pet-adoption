package io.github.wouterbauweraerts.samples.springpetadoption.adoptions;

import static net.datafaker.transformations.Field.field;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.event.PetAdoptedEvent;
import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.exceptions.OwnerNotFoundException;
import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.exceptions.PetNotFoundException;
import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.request.AdoptPetCommand;
import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.internal.AdoptionMapper;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.OwnerService;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.PetService;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;
import net.datafaker.Faker;
import net.datafaker.providers.base.BaseFaker;
import net.datafaker.transformations.Schema;

@ExtendWith(MockitoExtension.class)
class AdoptionServiceTest {
    private static final Faker FAKER = new Faker();

    @InjectMocks
    AdoptionService adoptionService;
    @Mock
    PetService petService;
    @Mock
    OwnerService ownerService;
    @Spy
    AdoptionMapper adoptionMapper = Mappers.getMapper(AdoptionMapper.class);
    @Mock
    ApplicationEventPublisher publisher;

    @Captor
    ArgumentCaptor<PetAdoptedEvent> eventCaptor;

    @Test
    void adopt_whenPetNotAdoptable_throwsExpected() {
        AdoptPetCommand command = new AdoptPetCommand(FAKER.number().positive(), FAKER.number().positive());

        when(petService.getPetForAdoption(command.petId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adoptionService.adopt(command))
                .isInstanceOf(PetNotFoundException.class)
                .hasMessage("Pet %d is not available for adoption.".formatted(command.petId()));

        verifyNoInteractions(adoptionMapper, publisher);
    }

    @Test
    void adopt_whenOwnerNotFound_throwsExpected() {
        AdoptPetCommand command = new AdoptPetCommand(FAKER.number().positive(), FAKER.number().positive());
        PetResponse pet = BaseFaker.populate(PetResponse.class, Schema.<Object, Object>of(
                field("id", () -> FAKER.number().positive()),
                field("name", () -> FAKER.animal().name()),
                field("type", () -> FAKER.options().option(PetType.class).name())
        ));

        when(petService.getPetForAdoption(command.petId())).thenReturn(Optional.of(pet));
        when(ownerService.getOwnerById(command.ownerId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adoptionService.adopt(command))
                .isInstanceOf(OwnerNotFoundException.class)
                .hasMessage("Owner %d is not available for adoption.".formatted(command.ownerId()));

        verifyNoInteractions(adoptionMapper, publisher);
    }

    @Test
    void adopt_whenValid_publishesExpected() {
        AdoptPetCommand command = new AdoptPetCommand(FAKER.number().positive(), FAKER.number().positive());
        PetResponse pet = BaseFaker.populate(PetResponse.class, Schema.<Object, Object>of(
                field("id", () -> FAKER.number().positive()),
                field("name", () -> FAKER.animal().name()),
                field("type", () -> FAKER.options().option(PetType.class).name())
        ));
        OwnerResponse owner = new OwnerResponse(
                FAKER.number().positive(),
                FAKER.name().fullName(),
                Map.of(
                        FAKER.options().option(PetType.class).name(),
                        List.of(FAKER.animal().name())
                )
        );

        when(petService.getPetForAdoption(command.petId())).thenReturn(Optional.of(pet));
        when(ownerService.getOwnerById(command.ownerId())).thenReturn(Optional.of(owner));

        assertThatCode(() -> adoptionService.adopt(command)).doesNotThrowAnyException();

        verify(publisher).publishEvent(eventCaptor.capture());

        assertThat(eventCaptor.getValue())
                .returns(command.petId(), PetAdoptedEvent::petId)
                .returns(command.ownerId(), PetAdoptedEvent::ownerId);
    }
}