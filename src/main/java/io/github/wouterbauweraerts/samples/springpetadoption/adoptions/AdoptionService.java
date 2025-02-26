package io.github.wouterbauweraerts.samples.springpetadoption.adoptions;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.exceptions.OwnerNotFoundException;
import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.exceptions.PetNotFoundException;
import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.request.AdoptPetCommand;
import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.internal.AdoptionMapper;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.OwnerService;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.PetService;
import jakarta.transaction.Transactional;

@Service
public class AdoptionService {
    private final PetService petService;
    private final OwnerService ownerService;
    private final AdoptionMapper adoptionMapper;
    private final ApplicationEventPublisher publisher;

    public AdoptionService(PetService petService, OwnerService ownerService, AdoptionMapper adoptionMapper, ApplicationEventPublisher publisher) {
        this.petService = petService;
        this.ownerService = ownerService;
        this.adoptionMapper = adoptionMapper;
        this.publisher = publisher;
    }

    @Transactional
    public void adopt(AdoptPetCommand command) {
        validateAdoption(command.ownerId(), command.petId());

        publisher.publishEvent(adoptionMapper.map(command));
    }

    private void validateAdoption(Integer ownerId, Integer petId) {
        petService.getPetForAdoption(petId).orElseThrow(() -> PetNotFoundException.withId(petId));
        ownerService.getOwnerById(ownerId).orElseThrow(() -> OwnerNotFoundException.withId(ownerId));
    }
}
