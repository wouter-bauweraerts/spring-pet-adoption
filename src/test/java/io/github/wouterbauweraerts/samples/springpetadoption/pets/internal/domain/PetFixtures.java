package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain;

import static org.instancio.Select.allInts;
import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.instancio.Model;

public class PetFixtures {
    public static final Model<Pet> PET_MODEL = Instancio.of(Pet.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .generate(field(Pet::getType), gen -> gen.enumOf(PetType.class))
            .toModel();

    public static Pet aPet() {
        return Instancio.create(PET_MODEL);
    }

    public static Pet anAdoptablePet() {
        return Instancio.of(PET_MODEL)
                .ignore(field(Pet::getOwnerId))
                .create();
    }

    public static Pet anUnpersistedPet() {
        return Instancio.of(PET_MODEL)
                .ignore(field(Pet::getId))
                .create();
    }

    public static Pet anUnpersistedAdoptablePet() {
        return Instancio.of(PET_MODEL)
                .ignore(field(Pet::getId))
                .ignore(field(Pet::getOwnerId))
                .create();
    }

    public static Pet anUnpersistedPetWithOwnerId(Integer ownerId) {
        return Instancio.of(PET_MODEL)
                .ignore(field(Pet::getId))
                .set(field(Pet::getOwnerId), ownerId)
                .create();
    }

    public static Pet aPetWithoutNameAndOwnerId() {
        return Instancio.of(PET_MODEL)
                .ignore(field(Pet::getName))
                .ignore(field(Pet::getOwnerId))
                .create();
    }

    public static Pet aPetWithoutType() {
        return Instancio.of(PET_MODEL)
                .ignore(field(Pet::getType))
                .create();
    }

    public static Pet aPetWithTypeAndOwnerId(PetType type, Integer ownerId) {
        return Instancio.of(PET_MODEL)
                .set(field(Pet::getType), type)
                .set(field(Pet::getOwnerId), ownerId)
                .create();
    }
}