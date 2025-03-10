package io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request;

import static net.datafaker.transformations.Field.field;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;
import net.datafaker.Faker;
import net.datafaker.providers.base.BaseFaker;
import net.datafaker.transformations.Schema;

public class AddPetRequestFixtures {
    private static final Faker FAKER = new Faker();

    private static final Schema<Object, ?> schema = Schema.of(
            field("name", () -> FAKER.dog().name()),
            field("type", () -> FAKER.options().option(PetType.class).name() + "")
    );

    public static AddPetRequest emptyRequest() {
        return new AddPetRequest(null, null);
    }

    public static AddPetRequest withNameAndNoType(String name) {
        return new AddPetRequest(name, null);
    }

    public static AddPetRequest withName(String name) {
        return new AddPetRequest(name, FAKER.options().option(PetType.class).name());
    }

    public static AddPetRequest anAddPetRequest() {
        return BaseFaker.populate(AddPetRequest.class, schema);
    }

    public static AddPetRequest withNameAndType(String name, String type) {
        return new AddPetRequest(name, type);
    }
}