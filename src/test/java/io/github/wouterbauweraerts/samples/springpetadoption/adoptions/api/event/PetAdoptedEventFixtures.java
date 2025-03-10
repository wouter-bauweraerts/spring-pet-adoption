package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.event;

import net.datafaker.Faker;

public class PetAdoptedEventFixtures {
    private static Faker FAKER = new Faker();

    public static PetAdoptedEvent aPetAdoptedEvent() {
        return new PetAdoptedEvent(FAKER.number().positive(), FAKER.number().positive());
    }
}