package io.github.wouterbauweraerts.samples.springpetadoption.owners.events;

import org.instancio.Instancio;

public class OwnerDeletedEventFixtures {
    public static OwnerDeletedEvent anOwnerDeletedEvent() {
        return Instancio.create(OwnerDeletedEvent.class);
    }
}