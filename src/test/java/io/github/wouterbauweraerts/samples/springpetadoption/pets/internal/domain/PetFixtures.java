package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain;

import static org.instancio.Select.allInts;
import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.instancio.fixture.builder.GenerateFixtureBuilder;
import io.github.wouterbauweraerts.instancio.fixture.builder.InstancioModel;

@GenerateFixtureBuilder(builderForType = Pet.class, fixtureClass = PetFixtures.class)
public class PetFixtures {
    @InstancioModel
    public static final Model<Pet> PET_MODEL = Instancio.of(Pet.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .generate(field(Pet::getType), gen -> gen.enumOf(PetType.class))
            .toModel();

    public static Pet aPet() {
        return PetFixtureBuilder.fixtureBuilder().build();
    }

    public static Pet anAdoptablePet() {
        return PetFixtureBuilder.fixtureBuilder().ignoreOwnerId().build();
    }

    public static Pet anUnpersistedPet() {
        return PetFixtureBuilder.fixtureBuilder().ignoreId().build();
    }

    public static Pet anUnpersistedAdoptablePet() {
        return PetFixtureBuilder.fixtureBuilder().ignoreId().ignoreOwnerId().build();
    }

    public static Pet anUnpersistedPetWithOwnerId(Integer ownerId) {
        return PetFixtureBuilder.fixtureBuilder()
                .ignoreId()
                .withOwnerId(ownerId)
                .build();
    }
}