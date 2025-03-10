package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetFixtures.aPet;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetFixtures.anAdoptablePet;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PetTest {

    @Test
    void petWithOwnerIsNotAvailableForAdoption() {
        Pet pet = aPet();

        assertThat(pet.isAvailableForAdoption()).isFalse();
    }

    @Test
    void petWithoutOwnerIsNotAvailableForAdoption() {
        Pet pet = anAdoptablePet();

        assertThat(pet.isAvailableForAdoption()).isTrue();
    }
}