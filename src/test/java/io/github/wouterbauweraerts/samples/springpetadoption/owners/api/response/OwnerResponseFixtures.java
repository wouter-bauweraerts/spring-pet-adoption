package io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response;

import static org.instancio.Select.allInts;
import static org.instancio.Select.field;

import java.util.List;
import java.util.Map;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.instancio.fixture.builder.GenerateFixtureBuilder;
import io.github.wouterbauweraerts.instancio.fixture.builder.InstancioModel;

@GenerateFixtureBuilder(builderForType = OwnerResponse.class, fixtureClass = OwnerResponseFixtures.class)
public class OwnerResponseFixtures {
    @InstancioModel
    public static final Model<OwnerResponse> OWNER_RESPONSE_MODEL = Instancio.of(OwnerResponse.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .set(field(OwnerResponse::getPets), Map.of())
            .toModel();

    public static OwnerResponse anOwnerResponse() {
        return OwnerResponseFixtureBuilder.fixtureBuilder().build();
    }

    public static List<OwnerResponse> aListOfOwnerResponses() {
        return Instancio.ofList(OWNER_RESPONSE_MODEL).create();
    }
}