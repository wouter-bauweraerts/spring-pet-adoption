package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.event;

public record PetAdoptedEvent(
        Integer ownerId,
        Integer petId
) {
}
