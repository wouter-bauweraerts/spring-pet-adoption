package io.github.wouterbauweraerts.samples.springpetadoption.pets.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.PetService;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request.AddPetRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;

@RestController
@RequestMapping("/pets")
public class PetController {
    private PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public Page<PetResponse> listPets(Pageable page) {
        return petService.getPets(page);
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetResponse> getPet(@PathVariable Integer petId) {
        return ResponseEntity.of(petService.getPet(petId));
    }

    @GetMapping("/available-for-adoption")
    public Page<PetResponse> findAvailablePets(Pageable page) {
        return petService.getPetsAvailableForAdoption(page);
    }

    public PetResponse addPet(AddPetRequest addPet) {
        return null;
    }
}
