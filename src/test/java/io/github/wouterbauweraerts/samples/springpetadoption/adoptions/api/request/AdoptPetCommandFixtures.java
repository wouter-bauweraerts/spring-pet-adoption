package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.request;

import net.datafaker.Faker;

public class AdoptPetCommandFixtures {
    private static final Faker FAKER = new Faker();

    public static AdoptPetCommand anEmptyAdoptPetCommand() {
        return new AdoptPetCommand(null, null);
    }

    public static AdoptPetCommand anInvalidAdoptPetCommandWithOwnerId() {
        return new AdoptPetCommand(FAKER.number().positive(), null);
    }

    public static AdoptPetCommand anInvalidAdoptPetCommandWithPetId() {
        return new AdoptPetCommand(null, FAKER.number().positive());
    }

    public static AdoptPetCommand anInvalidAdoptPetCommandWithNegativePetId() {
        return new AdoptPetCommand(FAKER.number().positive(), FAKER.number().negative());
    }

    public static AdoptPetCommand anInvalidAdoptPetCommandWithNegativeOwnerId() {
        return new AdoptPetCommand(FAKER.number().negative(), FAKER.number().positive());
    }

    public static AdoptPetCommand anInvalidAdoptPetCommandWithNegativeIds() {
        return new AdoptPetCommand(FAKER.number().negative(), FAKER.number().negative());
    }

    public static AdoptPetCommand aValidAdoptPetCommand() {
        return new AdoptPetCommand(FAKER.number().positive(), FAKER.number().positive());
    }
}