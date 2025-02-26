package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.exceptions;

public class PetNotFoundException extends InvalidAdoptionException {
    public PetNotFoundException(String s) {
        super(s);
    }

    public static PetNotFoundException withId(Integer id) {
        return new PetNotFoundException("Pet %d is not available for adoption.".formatted(id));
    }
}
