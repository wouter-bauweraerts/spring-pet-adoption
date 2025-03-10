package io.github.wouterbauweraerts.samples.springpetadoption.owners;

import static io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request.AddOwnerRequestFixtures.anAddOwnerRequest;
import static io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request.UpdateOwnerRequestFixtures.anUpdateOwnerRequest;
import static io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain.OwnerFixtures.anOwner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request.AddOwnerRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request.UpdateOwnerRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.events.OwnerDeletedEvent;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.OwnerMapper;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain.Owner;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.repository.OwnerRepository;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.PetService;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {
    @InjectMocks
    OwnerService ownerService;

    @Mock
    OwnerRepository ownerRepository;
    @Mock
    PetService petService;
    @Mock
    ApplicationEventPublisher eventPublisher;

    @Spy
    OwnerMapper ownerMapper = Mappers.getMapper(OwnerMapper.class);

    Owner owner1, owner2, owner3, owner4;

    @BeforeEach
    void setUp() {
        owner1 = anOwner();
        owner2 = anOwner();
        owner3 = anOwner();
        owner4 = anOwner();
    }

    @Test
    void getOwners_returnsExpected() {
        Pageable pageable = mock(Pageable.class);
        List<Owner> entities = List.of(owner1, owner2, owner3, owner4);
        List<OwnerResponse> expected = List.of(
                new OwnerResponse(owner1.getId(), owner1.getName(), Map.of("DOG", List.of("Roxy"))),
                new OwnerResponse(owner2.getId(), owner2.getName(), Map.of()),
                new OwnerResponse(owner3.getId(), owner3.getName(), Map.of("DOG", List.of("Production"))),
                new OwnerResponse(owner4.getId(), owner4.getName(), Map.of())
        );

        when(ownerRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(entities));
        when(petService.getPetsForOwner(anyInt()))
                .thenAnswer(invocation ->
                        {
                            if (invocation.getArgument(0).equals(owner1.getId())) {
                                return Map.of("DOG", List.of("Roxy"));
                            } else if (invocation.getArgument(0).equals(owner3.getId())) {
                                return Map.of("DOG", List.of("Production"));
                            }
                            return Map.of();
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
        OwnerResponse expected = new OwnerResponse(owner1.getId(), owner1.getName(), Map.of("DOG", List.of("Roxy")));

        when(ownerRepository.findById(any())).thenReturn(Optional.of(owner1));
        when(petService.getPetsForOwner(eq(owner1.getId()))).thenReturn(Map.of("DOG", List.of("Roxy")));

        assertThat(ownerService.getOwnerById(owner1.getId())).hasValue(expected);
        verify(ownerRepository).findById(owner1.getId());
    }

    @Test
    void addOwner_returnsExpected() {
        AddOwnerRequest request = anAddOwnerRequest();
        Owner unpersistedOwner = new Owner(null, request.name());
        Owner persistedOwner = new Owner(13, request.name());
        OwnerResponse expected = new OwnerResponse(persistedOwner.getId(), persistedOwner.getName(), Map.of());

        when(ownerRepository.save(any(Owner.class))).thenReturn(persistedOwner);

        assertThat(ownerService.addOwner(request)).isEqualTo(expected);

        verify(ownerRepository).save(unpersistedOwner);
    }

    @Test
    void updateOwner_notFound() {
        UpdateOwnerRequest request = anUpdateOwnerRequest();

        when(ownerRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ownerService.updateOwner(13, request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Owner with id 13 not found");
    }

    @Test
    void updateOwner_updatesExistingAndSaves() {
        Owner original = anOwner();
        UpdateOwnerRequest request = anUpdateOwnerRequest();
        Owner updatedOwner = new Owner(original.getId(), request.name());

        when(ownerRepository.findById(anyInt())).thenReturn(Optional.of(original));

        ownerService.updateOwner(13, request);

        assertThat(original.getName()).isEqualTo(request.name());
        verify(ownerRepository).save(updatedOwner);
    }

    @Test
    void deleteOwner_doesNotExist_nothingHappens() {
        when(ownerRepository.existsById(anyInt())).thenReturn(false);

        ownerService.deleteOwner(13);

        verify(ownerRepository).existsById(13);
        verifyNoMoreInteractions(ownerRepository);
        verifyNoMoreInteractions(eventPublisher);
    }

    @Test
    void deleteOwner_deletesFromRepositoryAndDispatchesEvent() {
        when(ownerRepository.existsById(anyInt())).thenReturn(true);

        ownerService.deleteOwner(13);

        verify(ownerRepository).existsById(13);
        verify(ownerRepository).deleteById(13);
        verify(eventPublisher).publishEvent(new OwnerDeletedEvent(13));
    }
}