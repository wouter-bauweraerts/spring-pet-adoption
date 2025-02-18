package io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request;

public record AddPetRequest(
        String name,
        String type
) {
}
