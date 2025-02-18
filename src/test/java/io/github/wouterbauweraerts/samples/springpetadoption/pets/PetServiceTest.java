package io.github.wouterbauweraerts.samples.springpetadoption.pets;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.CAT;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.DOG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
                new Pet(1, "Roxy", DOG),
                new Pet(2, "Rex", DOG),
                new Pet(3, "Filou", CAT)
        );

        List<PetResponse> petDtos = List.of(
                new PetResponse(1, "Roxy", DOG.name()),
                new PetResponse(2, "Rex", DOG.name()),
                new PetResponse(3, "Filou", CAT.name())
        );

        when(petRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(petEntities));

        assertThat(petService.getPets(pageRequest)).containsExactlyElementsOf(petDtos);
    }
}