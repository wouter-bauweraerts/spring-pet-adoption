package io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain;

import static org.instancio.Select.allInts;
import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.instancio.Model;

public class OwnerFixtures {
    public static final Model<Owner> OWNER_MODEL = Instancio.of(Owner.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .toModel();

    public static Owner anOwner() {
        return Instancio.create(OWNER_MODEL);
    }

    public static Owner anUnpersistedOwner() {
        return Instancio.of(OWNER_MODEL)
                .ignore(field(Owner::getId))
                .create();
    }

    public static Owner anOwnerWIithName(String name) {
        return Instancio.of(OWNER_MODEL)
                .set(field(Owner::getName), name)
                .create();
    }
}