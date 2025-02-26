package io.github.wouterbauweraerts.samples.springpetadoption.pets;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository.PetSpecification.adoptablePetSearchSpecification;
import static java.util.Optional.empty;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.request.AdoptablePetSearch;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.events.OwnerDeletedEvent;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request.AddPetRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.PetMapper;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository.PetRepository;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository.PetSpecification;
import jakarta.transaction.Transactional;

@Service
public class PetService {
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    public PetService(PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
    }

    public Page<PetResponse> getPets(Pageable pageable) {
        return petRepository.findAll(pageable)
                .map(petMapper::map);
    }

    public Optional<PetResponse> getPet(int petId) {
        return petRepository.findById(petId)
                .map(petMapper::map);
    }

    public Page<PetResponse> getPetsAvailableForAdoption(Pageable pageable) {
        return petRepository.findPetsAvailableForAdoption(pageable)
                .map(petMapper::map);
    }

    public PetResponse addPet(AddPetRequest addPetRequest) {
        return petMapper.map(
                petRepository.save(
                        petMapper.toEnity(addPetRequest)
                )
        );
    }

    public Map<String, List<String>> getPetsForOwner(Integer ownerId) {
        return petRepository.findAllByOwnerId(ownerId)
                .stream().collect(Collectors.groupingBy(pet -> pet.getType().name(), Collectors.mapping(Pet::getName, Collectors.toList())));
    }

    public Optional<PetResponse> getPetForAdoption(Integer petId) {
        Optional<Pet> pet = petRepository.findById(petId);
        return pet.flatMap(
                p -> p.isAvailableForAdoption()
                        ? pet
                        : empty()
        ).map(petMapper::map);
    }

    @Transactional
    @ApplicationModuleListener
    public void onOwnerDeleted(OwnerDeletedEvent ownerDeletedEvent) {
        petRepository.deleteAllByOwnerId(ownerDeletedEvent.ownerId());
    }

    public Page<PetResponse> searchAdoptablePets(AdoptablePetSearch search, Pageable pageable) {
        Page<Pet> pets;
        List<PetType> types = search.getTypes().stream()
                .map(PetType::valueOf)
                .toList();

        if (types.isEmpty() && search.getNames().isEmpty()) {
            pets = petRepository.findPetsAvailableForAdoption(pageable);
        } else {
            pets = petRepository.findAll(adoptablePetSearchSpecification(types, search.getNames()), pageable);
        }

        return pets.map(petMapper::map);
    }
}
