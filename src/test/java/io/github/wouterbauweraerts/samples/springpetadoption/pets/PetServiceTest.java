package io.github.wouterbauweraerts.samples.springpetadoption.pets;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.CAT;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.DOG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.instancio.Select.allInts;
import static org.instancio.Select.field;
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

import org.instancio.Instancio;
import org.instancio.Model;
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

@ExtendWith(MockitoExtension.class)
class PetServiceTest {
    @InjectMocks
    PetService petService;
    @Mock
    PetRepository petRepository;
    @Spy
    PetMapper petMapper = Mappers.getMapper(PetMapper.class);

    private static final Model<Pet> PET_MODEL = Instancio.of(Pet.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .generate(field(Pet::getType), gen -> gen.enumOf(PetType.class))
            .toModel();

    private static final Model<Pet> ADOPTABLE_PET_MODEL = Instancio.of(PET_MODEL).ignore(field(Pet::getOwnerId)).toModel();

    private static final Model<AddPetRequest> ADD_PET_REQUEST_MODEL = Instancio.of(AddPetRequest.class)
            .generate(field(AddPetRequest::type), gen -> gen.enumOf(PetType.class).as(Enum::name))
            .toModel();

    @Test
    void getPets_executesPaginatesQueryAndReturnsExpected() {
        Pageable pageRequest = mock(Pageable.class);
        List<Pet> petEntities = List.of(
                Instancio.create(PET_MODEL),
                Instancio.create(ADOPTABLE_PET_MODEL),
                Instancio.create(ADOPTABLE_PET_MODEL)
        );

        List<PetResponse> petDtos = petEntities.stream()
                .map(p -> new PetResponse(p.getId(), p.getName(), p.getType().name()))
                .toList();

        when(petRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(petEntities));

        assertThat(petService.getPets(pageRequest)).containsExactlyElementsOf(petDtos);
    }

    @Test
    void getPet_findsPetByIdAndReturnsExpected() {
        Pet petEntity = Instancio.create(PET_MODEL);
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
                Instancio.create(ADOPTABLE_PET_MODEL),
                Instancio.create(ADOPTABLE_PET_MODEL)
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
        AddPetRequest addPetRequest = Instancio.create(ADD_PET_REQUEST_MODEL);
        Pet newPet = new Pet(null, addPetRequest.name(), PetType.valueOf(addPetRequest.type()), null);
        Pet persistedPet = Instancio.create(PET_MODEL);
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
        Pet pet1 = Instancio.of(PET_MODEL)
                .set(field(Pet::getType), DOG)
                .set(field(Pet::getOwnerId), 1)
                .create();
        Pet pet2 = Instancio.of(PET_MODEL)
                .set(field(Pet::getType), DOG)
                .set(field(Pet::getOwnerId), 1)
                .create();
        Pet pet3 = Instancio.of(PET_MODEL)
                .set(field(Pet::getType), CAT)
                .set(field(Pet::getOwnerId), 1)
                .create();

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
        OwnerDeletedEvent ownerDeletedEvent = Instancio.create(OwnerDeletedEvent.class);

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
                Arguments.of(Optional.of(Instancio.create(PET_MODEL)))
        );
    }

    @Test
    void getPetForAdoption_returnsExpected() {
        Pet petEntity = Instancio.create(ADOPTABLE_PET_MODEL);
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
        PetAdoptedEvent event = Instancio.create(PetAdoptedEvent.class);

        when(petRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> petService.onPetAdopted(event))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Pet %d not found".formatted(event.petId()));
    }

    @Test
    void onPetAdoptedEvent_setsTheOwnerFromAPet() {
        PetAdoptedEvent event = Instancio.create(PetAdoptedEvent.class);
        Pet unadoptedPet = Instancio.of(PET_MODEL)
                .ignore(field(Pet::getOwnerId))
                .create();
        Pet adoptedPet = new Pet(unadoptedPet.getId(), unadoptedPet.getName(), unadoptedPet.getType(), event.ownerId());

        when(petRepository.findById(anyInt())).thenReturn(Optional.of(unadoptedPet));

        petService.onPetAdopted(event);

        verify(petRepository).save(adoptedPet);
    }
}