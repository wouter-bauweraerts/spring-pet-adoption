package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.request;

import static org.instancio.Select.allInts;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.instancio.fixture.builder.GenerateFixtureBuilder;
import io.github.wouterbauweraerts.instancio.fixture.builder.InstancioModel;

@GenerateFixtureBuilder(builderForType = AdoptPetCommand.class, fixtureClass = AdoptPetCommandFixtures.class)
public class AdoptPetCommandFixtures {
    @InstancioModel
    public static final Model<AdoptPetCommand> ADOPT_PET_COMMAND_MODEL = Instancio.of(AdoptPetCommand.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .toModel();

    public static AdoptPetCommand anAdoptPetCommand() {
        return AdoptPetCommandFixtureBuilder.fixtureBuilder().build();
    }

}