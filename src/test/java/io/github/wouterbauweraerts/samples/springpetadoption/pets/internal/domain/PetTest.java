package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain;

import static net.datafaker.transformations.Field.field;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import net.datafaker.Faker;
import net.datafaker.providers.base.BaseFaker;
import net.datafaker.transformations.Schema;

class PetTest {
    private static final Faker FAKER = new Faker();

    @Test
    void petWithOwnerIsNotAvailableForAdoption() {
        Pet pet = BaseFaker.populate(Pet.class, Schema.of(
                field("id", () -> FAKER.number().positive()),
                field("name", () -> FAKER.dog().name()),
                field("type", () -> FAKER.options().option(PetType.class)),
                field("ownerId", () -> FAKER.number().positive())
        ));

        assertThat(pet.isAvailableForAdoption()).isFalse();
    }

    @Test
    void petWithoutOwnerIsNotAvailableForAdoption() {
        Pet pet = BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("id", () -> FAKER.number().positive()),
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        );

        assertThat(pet.isAvailableForAdoption()).isTrue();
    }
}