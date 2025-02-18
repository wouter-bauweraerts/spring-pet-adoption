package io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response;

public record PetResponse(
        int id,
        String name,
        String type
) {
}
