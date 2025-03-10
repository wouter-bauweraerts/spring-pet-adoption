package io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request;

import net.datafaker.Faker;

public class UpdateOwnerRequestFixtures {
    private static Faker FAKER = new Faker();

    public static UpdateOwnerRequest anUpdateOwnerRequest() {
        return new UpdateOwnerRequest(FAKER.name().fullName());
    }
}