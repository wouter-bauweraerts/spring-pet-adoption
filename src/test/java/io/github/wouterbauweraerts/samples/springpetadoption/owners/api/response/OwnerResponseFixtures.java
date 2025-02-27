package io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response;

import java.util.List;
import java.util.Map;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;
import net.datafaker.Faker;

public class OwnerResponseFixtures {
    private static final Faker FAKER = new Faker();

    public static OwnerResponse anOwnerResponse() {
        return new OwnerResponse(
                FAKER.number().positive(),
                FAKER.name().fullName(),
                Map.of(
                        FAKER.options().option(PetType.class).name(),
                        List.of(FAKER.animal().name())
                )
        );
    }
}