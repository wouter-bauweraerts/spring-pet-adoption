package io.github.wouterbauweraerts.samples.springpetadoption.owners;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.OwnerMapper;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.repository.OwnerRepository;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.PetService;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final PetService petService;

    public OwnerService(OwnerRepository ownerRepository, OwnerMapper ownerMapper, PetService petService) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
        this.petService = petService;
    }

    public Page<OwnerResponse> getOwners(Pageable pageable) {
        return ownerRepository.findAll(pageable)
                .map(ownerMapper::map)
                .map(owner -> owner.addPets(petService.getPetsForOwner(owner.getId())));
    }

    public Optional<OwnerResponse> getOwnerById(Integer ownerId) {
        return ownerRepository.findById(ownerId)
                .map(ownerMapper::map)
                .map(owner -> owner.addPets(petService.getPetsForOwner(owner.getId())));
    }
}
