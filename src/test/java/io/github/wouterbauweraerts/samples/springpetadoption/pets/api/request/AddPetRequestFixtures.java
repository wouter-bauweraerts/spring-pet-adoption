package io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request;

import static org.instancio.Select.field;
import static org.instancio.Select.fields;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.instancio.fixture.builder.GenerateFixtureBuilder;
import io.github.wouterbauweraerts.instancio.fixture.builder.InstancioModel;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;

@GenerateFixtureBuilder(builderForType = AddPetRequest.class, fixtureClass = AddPetRequestFixtures.class)
public class AddPetRequestFixtures {
    @InstancioModel
    public static final Model<AddPetRequest> ADD_PET_REQUEST_MODEL = Instancio.of(AddPetRequest.class)
            .generate(field(AddPetRequest::type), gen -> gen.enumOf(PetType.class).as(Enum::name))
            .toModel();

    public static AddPetRequest anAddPetRequest() {
        return AddPetRequestFixtureBuilder.fixtureBuilder().build();
    }

    public static AddPetRequest anEmptyAddPetRequest() {
        return Instancio.of(ADD_PET_REQUEST_MODEL)
                .ignore(fields())
                .create();
    }
}