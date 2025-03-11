package io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response;

import static org.instancio.Select.allInts;
import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.instancio.fixture.builder.GenerateFixtureBuilder;
import io.github.wouterbauweraerts.instancio.fixture.builder.InstancioModel;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;

@GenerateFixtureBuilder(builderForType = PetResponse.class, fixtureClass = PetResponseFixtures.class)
public class PetResponseFixtures {
    @InstancioModel
    public static final Model<PetResponse> PET_RESPONSE_MODEL = Instancio.of(PetResponse.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .generate(field(PetResponse::type), gen -> gen.enumOf(PetType.class).as(Enum::name))
            .toModel();

    public static PetResponse aPetResponse() {
        return PetResponseFixtureBuilder.fixtureBuilder().build();
    }
}