package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.request.AdoptablePetSearch;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.PetService;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {
    private final PetService petService;

    public AdoptionController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/search")
    public Page<PetResponse> searchAdoptablePet(@Valid AdoptablePetSearch search, Pageable pageable) {
        return petService.searchAdoptablePets(search, pageable);
    }
}
