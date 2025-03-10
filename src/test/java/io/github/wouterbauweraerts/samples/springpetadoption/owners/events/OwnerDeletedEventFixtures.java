package io.github.wouterbauweraerts.samples.springpetadoption.owners.events;

import net.datafaker.Faker;

public class OwnerDeletedEventFixtures {
    private static final Faker FAKER = new Faker();

    public static OwnerDeletedEvent anOwnerDeletedEvent() {
        return new OwnerDeletedEvent(FAKER.number().positive());
    }
}