package io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain;

import static org.instancio.Select.allInts;
import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.samples.springpetadoption.util.AbstractFixtureBuilder;

public class OwnerFixtures {
    public static final Model<Owner> OWNER_MODEL = Instancio.of(Owner.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .toModel();

    public static OwnerFixtureBuilder fixtureBuilder() {
        return new OwnerFixtureBuilder();
    }

    public static Owner anOwner() {
        return Instancio.create(OWNER_MODEL);
    }

    public static Owner anUnpersistedOwner() {
        return fixtureBuilder().ignoreId().build();
    }

    public static class OwnerFixtureBuilder extends AbstractFixtureBuilder<Owner, OwnerFixtureBuilder> {
        @Override
        public Owner build() {
            return buildInternal(OWNER_MODEL);
        }

        @Override
        public OwnerFixtureBuilder self() {
            return this;
        }

        public OwnerFixtureBuilder withId(Integer id) {
            return this.setField(Owner::getId, id);
        }

        public OwnerFixtureBuilder withName(String name) {
            return this.setField(Owner::getName, name);
        }

        public OwnerFixtureBuilder ignoreId() {
            return this.withId(null);
        }

        public OwnerFixtureBuilder ignoreName() {
            return this.withName(null);
        }
    }
}