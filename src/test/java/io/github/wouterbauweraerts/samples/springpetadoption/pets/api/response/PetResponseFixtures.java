package io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response;

import static org.instancio.Select.allInts;
import static org.instancio.Select.field;

import org.instancio.Instancio;
import org.instancio.Model;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;

public class PetResponseFixtures {
    public static final Model<PetResponse> PET_RESPONSE_MODEL = Instancio.of(PetResponse.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .generate(field(PetResponse::type), gen -> gen.enumOf(PetType.class).as(Enum::name))
            .toModel();

    public static PetResponse aPetResponse() {
        return Instancio.create(PET_RESPONSE_MODEL);
    }
}