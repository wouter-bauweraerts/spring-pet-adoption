package io.github.wouterbauweraerts.samples.springpetadoption.pets;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request.AddPetRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.PetMapper;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository.PetRepository;

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
}
