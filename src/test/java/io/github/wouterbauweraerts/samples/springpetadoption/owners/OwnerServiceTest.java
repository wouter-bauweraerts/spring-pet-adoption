package io.github.wouterbauweraerts.samples.springpetadoption.owners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.OwnerMapper;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain.Owner;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.repository.OwnerRepository;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.PetService;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {
    @InjectMocks
    OwnerService ownerService;

    @Mock
    OwnerRepository ownerRepository;
    @Mock
    PetService petService;

    @Spy
    OwnerMapper ownerMapper = Mappers.getMapper(OwnerMapper.class);

    @Test
    void getOwners_returnsExpected() {
        Pageable pageable = mock(Pageable.class);
        List<Owner> entities = List.of(
                new Owner(1, "Wouter"),
                new Owner(2, "Alina"),
                new Owner(3, "Josh"),
                new Owner(4, "Matthias")
        );
        List<OwnerResponse> expected = List.of(
                new OwnerResponse(1, "Wouter", Map.of("DOG", List.of("Roxy"))),
                new OwnerResponse(2, "Alina", Map.of()),
                new OwnerResponse(3, "Josh", Map.of("DOG", List.of("Production"))),
                new OwnerResponse(4, "Matthias", Map.of())
        );

        when(ownerRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(entities));
        when(petService.getPetsForOwner(anyInt()))
                .thenAnswer(invocation -> switch ((int) invocation.getArgument(0)) {
                            case 1 -> Map.of("DOG", List.of("Roxy"));
                            case 3 -> Map.of("DOG", List.of("Production"));
                            default -> Map.of();
                        }
                );
        assertThat(ownerService.getOwners(pageable)).containsExactlyInAnyOrderElementsOf(expected);
        verify(ownerRepository).findAll(pageable);
    }

    @Test
    void getOwnerById_notFoundReturnsEmpty() {
        when(ownerRepository.findById(any())).thenReturn(Optional.empty());

        assertThat(ownerService.getOwnerById(1)).isEmpty();
    }

    @Test
    void getOwnerById_returnsExpected() {
        Owner wouter = new Owner(1, "Wouter");
        OwnerResponse expected = new OwnerResponse(1, "Wouter", Map.of("DOG", List.of("Roxy")));

        when(ownerRepository.findById(any())).thenReturn(Optional.of(wouter));
        when(petService.getPetsForOwner(eq(1))).thenReturn(Map.of("DOG", List.of("Roxy")));

        assertThat(ownerService.getOwnerById(1)).hasValue(expected);
        verify(ownerRepository).findById(1);
    }
}