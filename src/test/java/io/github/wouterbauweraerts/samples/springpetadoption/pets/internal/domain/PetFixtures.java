package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain;

import static net.datafaker.transformations.Field.field;

import net.datafaker.Faker;
import net.datafaker.providers.base.BaseFaker;
import net.datafaker.transformations.Schema;

public class PetFixtures {
    private static final Faker FAKER = new Faker();
    private static final Schema<Object, ?> schema = Schema.of(
            field("id", () -> FAKER.number().positive()),
            field("name", () -> FAKER.dog().name()),
            field("type", () -> FAKER.options().option(PetType.class)),
            field("ownerId", () -> FAKER.number().positive())
    );

    public static Pet aPet() {
        return BaseFaker.populate(Pet.class, schema);
    }

    public static Pet anAdoptablePet() {
        return BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("id", () -> FAKER.number().positive()),
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        );
    }

    public static Pet aPetWithOwnerAndType(int ownerId, PetType type) {
        return BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("id", () -> FAKER.number().positive()),
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> type),
                        field("ownerId", () -> ownerId)
                )
        );
    }

    public static Pet anUnpersistedPet() {
        return BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        );
    }

    public static Pet anUnpersistedPetWithOwner(int ownerId) {
        return BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> ownerId)
                )
        );
    }

    public static Pet anUnpersistedAdoptablePet() {
        return BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        );
    }
}