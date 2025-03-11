package io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.instancio.fixture.builder.GenerateFixtureBuilder;
import io.github.wouterbauweraerts.instancio.fixture.builder.InstancioModel;

@GenerateFixtureBuilder(builderForType = UpdateOwnerRequest.class, fixtureClass = UpdateOwnerRequestFixtures.class)
public class UpdateOwnerRequestFixtures {
    @InstancioModel
    public static final Model<UpdateOwnerRequest> UPDATE_OWNER_REQUEST_MODEL = Instancio.of(UpdateOwnerRequest.class)
            .toModel();

    public static UpdateOwnerRequest anUpdateOwnerRequest() {
        return UpdateOwnerRequestFixtureBuilder.fixtureBuilder().build();
    }
}