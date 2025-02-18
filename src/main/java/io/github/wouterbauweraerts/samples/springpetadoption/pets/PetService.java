package io.github.wouterbauweraerts.samples.springpetadoption.pets;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;

@Service
public class PetService {
    public Page<PetResponse> getPets(Pageable pageRequest) {
        return null;
    }
}
