package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.allInts;

import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.junit.jupiter.api.Test;

class PetTest {

    private static final Model<Pet> PET_MODEL = Instancio.of(Pet.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .generate(Select.field(Pet::getType), gen -> gen.enumOf(PetType.class))
            .toModel();

    @Test
    void petWithOwnerIsNotAvailableForAdoption() {
        Pet pet = Instancio.create(PET_MODEL);

        assertThat(pet.isAvailableForAdoption()).isFalse();
    }

    @Test
    void petWithoutOwnerIsNotAvailableForAdoption() {
        Pet pet = Instancio.of(PET_MODEL)
                .ignore(Select.field(Pet::getOwnerId))
                .create();

        assertThat(pet.isAvailableForAdoption()).isTrue();
    }
}