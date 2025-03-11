package io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain;

import static org.instancio.Select.allInts;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.instancio.fixture.builder.GenerateFixtureBuilder;
import io.github.wouterbauweraerts.instancio.fixture.builder.InstancioModel;

@GenerateFixtureBuilder(builderForType = Owner.class, fixtureClass = OwnerFixtures.class)
public class OwnerFixtures {
    @InstancioModel
    public static final Model<Owner> OWNER_MODEL = Instancio.of(Owner.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .toModel();

    public static Owner anOwner() {
        return OwnerFixtureBuilder.fixtureBuilder().build();
    }

    public static Owner anUnpersistedOwner() {
        return OwnerFixtureBuilder.fixtureBuilder().ignoreId().build();
    }
}