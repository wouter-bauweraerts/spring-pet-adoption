package io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request;

import net.datafaker.Faker;

public class AddOwnerRequestFixtures {
    private static Faker FAKER = new Faker();

    public static AddOwnerRequest anAddOwnerRequest() {
        return new AddOwnerRequest(FAKER.name().fullName());
    }
}