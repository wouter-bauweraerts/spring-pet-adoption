package io.github.wouterbauweraerts.samples.springpetadoption.pets;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.CAT;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.DOG;
import static net.datafaker.transformations.Field.field;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.event.PetAdoptedEvent;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.events.OwnerDeletedEvent;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request.AddPetRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.PetMapper;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository.PetRepository;
import net.datafaker.Faker;
import net.datafaker.providers.base.BaseFaker;
import net.datafaker.transformations.Schema;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {
    private static final Faker FAKER = new Faker();
    @InjectMocks
    PetService petService;
    @Mock
    PetRepository petRepository;
    @Spy
    PetMapper petMapper = Mappers.getMapper(PetMapper.class);

    @Test
    void getPets_executesPaginatesQueryAndReturnsExpected() {
        Pageable pageRequest = mock(Pageable.class);
        List<Pet> petEntities = List.of(
                BaseFaker.populate(Pet.class, Schema.of(
                        field("id", () -> FAKER.number().positive()),
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )),
                BaseFaker.populate(
                        Pet.class,
                        Schema.of(
                                field("id", () -> FAKER.number().positive()),
                                field("name", () -> FAKER.dog().name()),
                                field("type", () -> FAKER.options().option(PetType.class))
                        )
                ),
                BaseFaker.populate(
                        Pet.class,
                        Schema.of(
                                field("id", () -> FAKER.number().positive()),
                                field("name", () -> FAKER.dog().name()),
                                field("type", () -> FAKER.options().option(PetType.class))
                        )
                )
        );

        List<PetResponse> petDtos = petEntities.stream()
                .map(p -> new PetResponse(p.getId(), p.getName(), p.getType().name()))
                .toList();

        when(petRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(petEntities));

        assertThat(petService.getPets(pageRequest)).containsExactlyElementsOf(petDtos);
    }

    @Test
    void getPet_findsPetByIdAndReturnsExpected() {
        Pet petEntity = BaseFaker.populate(Pet.class, Schema.of(
                field("id", () -> FAKER.number().positive()),
                field("name", () -> FAKER.dog().name()),
                field("type", () -> FAKER.options().option(PetType.class)),
                field("ownerId", () -> FAKER.number().positive())
        ));
        PetResponse petDto = new PetResponse(petEntity.getId(), petEntity.getName(), petEntity.getType().name());

        when(petRepository.findById(anyInt())).thenReturn(java.util.Optional.of(petEntity));

        assertThat(petService.getPet(petEntity.getId())).hasValue(petDto);
    }

    @Test
    void getPetWhenNothingFound_returnsEmpty() {
        when(petRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThat(petService.getPet(1)).isEmpty();

        verifyNoInteractions(petMapper);
    }

    @Test
    void getPetsAvailableForAdoption_returnsExpected() {
        List<Pet> petEntities = List.of(
                BaseFaker.populate(
                        Pet.class,
                        Schema.of(
                                field("id", () -> FAKER.number().positive()),
                                field("name", () -> FAKER.dog().name()),
                                field("type", () -> FAKER.options().option(PetType.class))
                        )
                ),
                BaseFaker.populate(
                        Pet.class,
                        Schema.of(
                                field("id", () -> FAKER.number().positive()),
                                field("name", () -> FAKER.dog().name()),
                                field("type", () -> FAKER.options().option(PetType.class))
                        )
                )
        );

        List<PetResponse> petDtos = petEntities.stream()
                .map(p -> new PetResponse(p.getId(), p.getName(), p.getType().name()))
                .toList();

        when(petRepository.findPetsAvailableForAdoption(any(Pageable.class)))
                .thenReturn(new PageImpl<>(petEntities));

        assertThat(petService.getPetsAvailableForAdoption(mock(Pageable.class))).containsExactlyElementsOf(petDtos);
    }

    @Test
    void addPetCreatedNewPetAndPersist() {
        AddPetRequest addPetRequest = BaseFaker.populate(AddPetRequest.class, Schema.of(
                field("name", () -> FAKER.dog().name()),
                field("type", () -> FAKER.options().option(PetType.class).name())
        ));
        Pet newPet = new Pet(null, addPetRequest.name(), PetType.valueOf(addPetRequest.type()), null);
        Pet persistedPet = BaseFaker.populate(Pet.class, Schema.of(
                field("id", () -> FAKER.number().positive()),
                field("name", () -> FAKER.dog().name()),
                field("type", () -> FAKER.options().option(PetType.class)),
                field("ownerId", () -> FAKER.number().positive())
        ));
        PetResponse expectedResponse = new PetResponse(
                persistedPet.getId(),
                persistedPet.getName(),
                persistedPet.getType().name()
        );

        when(petRepository.save(any(Pet.class))).thenReturn(persistedPet);

        assertThat(petService.addPet(addPetRequest)).isEqualTo(expectedResponse);

        verify(petRepository).save(newPet);
    }

    @Test
    void getPetsForOwner_noPets_returnsEmptyMap() {
        when(petRepository.findAllByOwnerId(anyInt())).thenReturn(List.of());

        assertThat(petService.getPetsForOwner(1)).isEmpty();
    }

    @Test
    void getPetsForOwner_returnsExpectedMap() {
        Pet pet1 = BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("id", () -> FAKER.number().positive()),
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> DOG),
                        field("ownerId", () -> 1)
                )
        );
        Pet pet2 = BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("id", () -> FAKER.number().positive()),
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> DOG),
                        field("ownerId", () -> 1)
                )
        );
        Pet pet3 = BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("id", () -> FAKER.number().positive()),
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> CAT),
                        field("ownerId", () -> 1)
                )
        );

        List<Pet> pets = List.of(pet1, pet2, pet3);
        when(petRepository.findAllByOwnerId(anyInt())).thenReturn(pets);

        assertThat(petService.getPetsForOwner(1)).containsExactlyInAnyOrderEntriesOf(
                Map.of(
                        "DOG", List.of(pet1.getName(), pet2.getName()),
                        "CAT", List.of(pet3.getName())
                )
        );
    }

    @Test
    void onOwnerDeletedAllPetsOfOwnerAreDeleted() {
        OwnerDeletedEvent ownerDeletedEvent = new OwnerDeletedEvent(FAKER.number().positive());

        petService.onOwnerDeleted(ownerDeletedEvent);

        verify(petRepository).deleteAllByOwnerId(ownerDeletedEvent.ownerId());
    }

    @ParameterizedTest
    @MethodSource("noAdoptablePetSource")
    void getPetForAdoption_returnsEmpty(Optional<Pet> optionalPet) {

        when(petRepository.findById(anyInt())).thenReturn(optionalPet);
        assertThat(petService.getPetForAdoption(666)).isEmpty();
    }

    private static Stream<Arguments> noAdoptablePetSource() {
        return Stream.of(
                Arguments.of(Optional.empty()),
                Arguments.of(Optional.of(BaseFaker.populate(Pet.class, Schema.of(
                        field("id", () -> FAKER.number().positive()),
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                ))))
        );
    }

    @Test
    void getPetForAdoption_returnsExpected() {
        Pet petEntity = BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("id", () -> FAKER.number().positive()),
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        );
        PetResponse petDto = new PetResponse(
                petEntity.getId(),
                petEntity.getName(),
                petEntity.getType().name()
        );

        when(petRepository.findById(anyInt())).thenReturn(Optional.of(petEntity));

        assertThat(petService.getPetForAdoption(petEntity.getId())).hasValue(petDto);
    }

    @Test
    void onPetAdoptedEvent_whenPetNotFound_throwsExpected() {
        PetAdoptedEvent event = new PetAdoptedEvent(FAKER.number().positive(), FAKER.number().positive());

        when(petRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.onPetAdopted(event))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Pet %d not found".formatted(event.petId()));
    }

    @Test
    void onPetAdoptedEvent_setsTheOwnerFromAPet() {
        PetAdoptedEvent event = new PetAdoptedEvent(FAKER.number().positive(), FAKER.number().positive());
        Pet unadoptedPet = BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("id", () -> FAKER.number().positive()),
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        );
        Pet adoptedPet = new Pet(unadoptedPet.getId(), unadoptedPet.getName(), unadoptedPet.getType(), event.ownerId());

        when(petRepository.findById(anyInt())).thenReturn(Optional.of(unadoptedPet));

        petService.onPetAdopted(event);

        verify(petRepository).save(adoptedPet);
    }
}