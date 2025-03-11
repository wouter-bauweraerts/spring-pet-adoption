package io.github.wouterbauweraerts.samples.springpetadoption.owners.events;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.instancio.fixture.builder.GenerateFixtureBuilder;
import io.github.wouterbauweraerts.instancio.fixture.builder.InstancioModel;

@GenerateFixtureBuilder(builderForType = OwnerDeletedEvent.class, fixtureClass = OwnerDeletedEventFixtures.class)
public class OwnerDeletedEventFixtures {
    @InstancioModel
    public static final Model<OwnerDeletedEvent> OWNER_DELETED_EVENT_MODEL = Instancio.of(OwnerDeletedEvent.class).toModel();

    public static OwnerDeletedEvent anOwnerDeletedEvent() {
        return OwnerDeletedEventFixtureBuilder.fixtureBuilder().build();
    }
}