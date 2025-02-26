package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.exceptions;

public class OwnerNotFoundException extends InvalidAdoptionException {
    public OwnerNotFoundException(String s) {
        super(s);
    }

    public static OwnerNotFoundException withId(Integer id) {
        return new OwnerNotFoundException("Owner %d is not available for adoption.".formatted(id));
    }
}
