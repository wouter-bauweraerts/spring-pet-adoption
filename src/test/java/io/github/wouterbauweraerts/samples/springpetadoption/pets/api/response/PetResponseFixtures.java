package io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response;

import static net.datafaker.transformations.Field.field;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;
import net.datafaker.Faker;
import net.datafaker.transformations.JavaObjectTransformer;
import net.datafaker.transformations.Schema;

public class PetResponseFixtures {
    private static Faker faker = new Faker();
    private static JavaObjectTransformer transformer = new JavaObjectTransformer();

    private static Schema<Object, Object> schema= Schema.of(
            field("id", () -> faker.number().positive()),
            field("name", () -> faker.animal().name()),
            field("type", () -> faker.options().option(PetType.class).name())
    );

    public static PetResponse aPetResponse() {
        return (PetResponse) transformer.apply(PetResponse.class, schema);
    }
}