package io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.instancio.fixture.builder.GenerateFixtureBuilder;
import io.github.wouterbauweraerts.instancio.fixture.builder.InstancioModel;

@GenerateFixtureBuilder(builderForType = AddOwnerRequest.class, fixtureClass = AddOwnerRequestFixtures.class)
public class AddOwnerRequestFixtures {
    @InstancioModel
    public static final Model<AddOwnerRequest> ADD_OWNER_REQUEST_MODEL = Instancio.of(AddOwnerRequest.class)
            .toModel();

    public static AddOwnerRequest anAddOwnerRequest() {
        return AddOwnerRequestFixtureBuilder.fixtureBuilder().build();
    }
}