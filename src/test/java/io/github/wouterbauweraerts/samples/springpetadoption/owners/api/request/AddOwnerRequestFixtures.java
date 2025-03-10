package io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request;

import static org.instancio.Select.field;

import org.instancio.Instancio;

public class AddOwnerRequestFixtures {

    public static AddOwnerRequest anAddOwnerRequest() {
        return Instancio.create(AddOwnerRequest.class);
    }

    public static AddOwnerRequest anAddOwnerRequest(String name) {
        return Instancio.of(AddOwnerRequest.class)
                .set(field(AddOwnerRequest::name), name)
                .create();
    }
}