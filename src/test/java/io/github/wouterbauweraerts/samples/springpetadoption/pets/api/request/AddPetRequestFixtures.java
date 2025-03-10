package io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request;

import static org.instancio.Select.field;
import static org.instancio.Select.fields;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;

public class AddPetRequestFixtures {
    public static final Model<AddPetRequest> ADD_PET_REQUEST_MODEL = Instancio.of(AddPetRequest.class)
            .generate(field(AddPetRequest::type), gen -> gen.enumOf(PetType.class).as(Enum::name))
            .toModel();

    public static AddPetRequest anAddPetRequest() {
        return Instancio.create(ADD_PET_REQUEST_MODEL);
    }

    public static AddPetRequest anEmptyAddPetRequest() {
        return Instancio.of(ADD_PET_REQUEST_MODEL)
                .ignore(fields())
                .create();
    }

    public static AddPetRequest anAddPetRequestWithNameAndType(String name, String type) {
        return Instancio.of(ADD_PET_REQUEST_MODEL)
                .set(field(AddPetRequest::name), name)
                .set(field(AddPetRequest::type), type)
                .create();
    }

    public static AddPetRequest anAddPetRequestWithoutName() {
        return Instancio.of(ADD_PET_REQUEST_MODEL)
                .ignore(field(AddPetRequest::name))
                .create();
    }
}