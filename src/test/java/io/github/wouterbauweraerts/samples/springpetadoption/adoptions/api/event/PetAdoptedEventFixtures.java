package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.event;

import static org.instancio.Select.allInts;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.instancio.fixture.builder.GenerateFixtureBuilder;
import io.github.wouterbauweraerts.instancio.fixture.builder.InstancioModel;

@GenerateFixtureBuilder(builderForType = PetAdoptedEvent.class, fixtureClass = PetAdoptedEventFixtures.class)
public class PetAdoptedEventFixtures {
    @InstancioModel
    public static final Model<PetAdoptedEvent> PET_ADOPTED_EVENT_MODEL = Instancio.of(PetAdoptedEvent.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .toModel();

    public static PetAdoptedEvent aPetAdoptedEvent() {
        return PetAdoptedEventFixtureBuilder.fixtureBuilder().build();
    }
}