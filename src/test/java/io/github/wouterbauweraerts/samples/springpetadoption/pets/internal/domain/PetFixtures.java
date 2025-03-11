package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain;

import static org.instancio.Select.allInts;
import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.samples.springpetadoption.util.AbstractFixtureBuilder;

public class PetFixtures {
    public static final Model<Pet> PET_MODEL = Instancio.of(Pet.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .generate(field(Pet::getType), gen -> gen.enumOf(PetType.class))
            .toModel();

    public static PetFixtureBuilder fixtureBuilder() {
        return new PetFixtureBuilder();
    }

    public static Pet aPet() {
        return Instancio.create(PET_MODEL);
    }

    public static Pet anAdoptablePet() {
        return fixtureBuilder().ignoreOwnerId().build();
    }

    public static Pet anUnpersistedPet() {
        return fixtureBuilder().ignoreId().build();
    }

    public static Pet anUnpersistedAdoptablePet() {
        return fixtureBuilder().ignoreId().ignoreOwnerId().build();
    }

    public static class PetFixtureBuilder extends AbstractFixtureBuilder<Pet, PetFixtureBuilder> {
        @Override
        public Pet build() {
            return buildInternal(PET_MODEL);
        }

        @Override
        public PetFixtureBuilder self() {
            return this;
        }

        public PetFixtureBuilder withId(Integer id) {
            return this.setField(Pet::getId, id);
        }

        public PetFixtureBuilder withName(String name) {
            return this.setField(Pet::getName, name);
        }

        public PetFixtureBuilder withType(PetType type) {
            return this.setField(Pet::getType, type);
        }

        public PetFixtureBuilder withOwnerId(Integer ownerId) {
            return this.setField(Pet::getOwnerId, ownerId);
        }

        public PetFixtureBuilder ignoreId() {
            return this.withId(null);
        }

        public PetFixtureBuilder ignoreName() {
            return this.withName(null);
        }

        public PetFixtureBuilder ignoreType() {
            return this.withType(null);
        }

        public PetFixtureBuilder ignoreOwnerId() {
            return this.withOwnerId(null);
        }
    }
}