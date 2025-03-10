package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.event;

import static org.instancio.Select.allInts;

import org.instancio.Instancio;
import org.instancio.Model;

public class PetAdoptedEventFixtures {
    public static final Model<PetAdoptedEvent> PET_ADOPTED_EVENT_MODEL = Instancio.of(PetAdoptedEvent.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .toModel();

    public static PetAdoptedEvent aPetAdoptedEvent() {
        return Instancio.create(PET_ADOPTED_EVENT_MODEL);
    }
}