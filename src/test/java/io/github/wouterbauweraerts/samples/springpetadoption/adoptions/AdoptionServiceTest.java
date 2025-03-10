package io.github.wouterbauweraerts.samples.springpetadoption.adoptions;

import static io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.request.AdoptPetCommandFixtures.anAdoptPetCommand;
import static io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponseFixtures.anOwnerResponse;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponseFixtures.aPetResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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

@ExtendWith(MockitoExtension.class)
class AdoptionServiceTest {
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
        AdoptPetCommand command = anAdoptPetCommand();

        when(petService.getPetForAdoption(command.petId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adoptionService.adopt(command))
                .isInstanceOf(PetNotFoundException.class)
                .hasMessage("Pet %d is not available for adoption.".formatted(command.petId()));

        verifyNoInteractions(adoptionMapper, publisher);
    }

    @Test
    void adopt_whenOwnerNotFound_throwsExpected() {
        AdoptPetCommand command = anAdoptPetCommand();
        PetResponse pet = aPetResponse();

        when(petService.getPetForAdoption(command.petId())).thenReturn(Optional.of(pet));
        when(ownerService.getOwnerById(command.ownerId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adoptionService.adopt(command))
                .isInstanceOf(OwnerNotFoundException.class)
                .hasMessage("Owner %d is not available for adoption.".formatted(command.ownerId()));

        verifyNoInteractions(adoptionMapper, publisher);
    }

    @Test
    void adopt_whenValid_publishesExpected() {
        AdoptPetCommand command = anAdoptPetCommand();
        PetResponse pet = aPetResponse();
        OwnerResponse owner = anOwnerResponse();

        when(petService.getPetForAdoption(command.petId())).thenReturn(Optional.of(pet));
        when(ownerService.getOwnerById(command.ownerId())).thenReturn(Optional.of(owner));

        assertThatCode(() -> adoptionService.adopt(command)).doesNotThrowAnyException();

        verify(publisher).publishEvent(eventCaptor.capture());

        assertThat(eventCaptor.getValue())
                .returns(command.petId(), PetAdoptedEvent::petId)
                .returns(command.ownerId(), PetAdoptedEvent::ownerId);
    }
}