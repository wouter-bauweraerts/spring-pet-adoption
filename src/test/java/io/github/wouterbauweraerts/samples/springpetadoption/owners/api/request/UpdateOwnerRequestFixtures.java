package io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request;

import org.instancio.Instancio;

public class UpdateOwnerRequestFixtures {
    public static UpdateOwnerRequest anUpdateOwnerRequest() {
        return Instancio.create(UpdateOwnerRequest.class);
    }
}