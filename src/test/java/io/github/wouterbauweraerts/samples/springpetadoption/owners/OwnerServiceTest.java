package io.github.wouterbauweraerts.samples.springpetadoption.owners;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.OwnerMapper;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain.Owner;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.repository.OwnerRepository;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {
    @InjectMocks
    OwnerService ownerService;
    @Mock
    OwnerRepository ownerRepository;
    @Spy
    OwnerMapper ownerMapper = Mappers.getMapper(OwnerMapper.class);

    @Test
    void getOwners_returnsExpected() {
        Pageable pageable = mock(Pageable.class);
        List<Owner> entities = List.of(
                new Owner(1, "Wouter"),
                new Owner(1, "Alina"),
                new Owner(1, "Josh"),
                new Owner(1, "Matthias")
        );
        List<OwnerResponse> expected = List.of(
                new OwnerResponse(1, "Wouter"),
                new OwnerResponse(1, "Alina"),
                new OwnerResponse(1, "Josh"),
                new OwnerResponse(1, "Matthias")
        );

        when(ownerRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(entities));

        assertThat(ownerService.getOwners(pageable)).containsExactlyInAnyOrderElementsOf(expected);
        verify(ownerRepository).findAll(pageable);
    }
}