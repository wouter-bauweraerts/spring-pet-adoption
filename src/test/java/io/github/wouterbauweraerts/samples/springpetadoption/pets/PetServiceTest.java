package io.github.wouterbauweraerts.samples.springpetadoption.pets;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.CAT;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.DOG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request.AddPetRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.PetMapper;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository.PetRepository;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {
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
                new Pet(1, "Roxy", DOG, 1),
                new Pet(2, "Rex", DOG, null),
                new Pet(3, "Filou", CAT, null)
        );

        List<PetResponse> petDtos = List.of(
                new PetResponse(1, "Roxy", DOG.name()),
                new PetResponse(2, "Rex", DOG.name()),
                new PetResponse(3, "Filou", CAT.name())
        );

        when(petRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(petEntities));

        assertThat(petService.getPets(pageRequest)).containsExactlyElementsOf(petDtos);
    }

    @Test
    void getPet_findsPetByIdAndReturnsExpected() {
        Pet petEntity = new Pet(1, "Roxy", DOG, 1);
        PetResponse petDto = new PetResponse(1, "Roxy", DOG.name());

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
                new Pet(2, "Rex", DOG, null),
                new Pet(3, "Filou", CAT, null)
        );

        List<PetResponse> petDtos = List.of(
                new PetResponse(2, "Rex", DOG.name()),
                new PetResponse(3, "Filou", CAT.name())
        );

        when(petRepository.findPetsAvailableForAdoption(any(Pageable.class)))
                .thenReturn(new PageImpl<>(petEntities));

        assertThat(petService.getPetsAvailableForAdoption(mock(Pageable.class))).containsExactlyElementsOf(petDtos);
    }

    @Test
    void addPetCreatedNewPetAndPersist() {
        AddPetRequest addPetRequest = new AddPetRequest("Goofy", "DOG");
        Pet newPet = new Pet(null, "Goofy", DOG, null);
        Pet persistedPet = new Pet(42, "Goofy", DOG, null);
        PetResponse expectedResponse = new PetResponse(42, "Goofy", "DOG");

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
        Pet roxy = new Pet(1, "Roxy", DOG, 1);
        Pet rex = new Pet(2, "Rex", DOG, 1);
        Pet filou = new Pet(3, "Filou", CAT, 1);

        List<Pet> pets = List.of(roxy, rex, filou);
        when(petRepository.findAllByOwnerId(anyInt())).thenReturn(pets);

        assertThat(petService.getPetsForOwner(1)).containsExactlyInAnyOrderEntriesOf(
                Map.of(
                        "DOG", List.of("Roxy", "Rex"),
                        "CAT", List.of("Filou")
                )
        );
    }
}