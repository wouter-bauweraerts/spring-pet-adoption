package io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain;

import net.datafaker.Faker;

public class OwnerFixtures {
    private static final Faker FAKER = new Faker();

    public static Owner anOwner() {
        return new Owner(
                FAKER.number().positive(),
                FAKER.name().fullName()
        );
    }

    public static Owner anUnperstedOwner() {
        return new Owner(
                null,
                FAKER.name().fullName()
        );
    }

    public static Owner anOwnerWithoutName() {
        return new Owner(
                FAKER.number().positive(), null
        );
    }
}