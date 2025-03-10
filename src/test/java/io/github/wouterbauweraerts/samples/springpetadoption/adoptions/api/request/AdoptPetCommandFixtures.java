package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.request;

import static org.instancio.Select.allInts;
import static org.instancio.Select.field;
import static org.instancio.Select.fields;

import org.instancio.Instancio;
import org.instancio.Model;
import org.springframework.aop.config.AspectComponentDefinition;

public class AdoptPetCommandFixtures {
    public static final Model<AdoptPetCommand> ADOPT_PET_COMMAND_MODEL = Instancio.of(AdoptPetCommand.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .toModel();

    public static AdoptPetCommand anAdoptPetCommand() {
        return Instancio.create(ADOPT_PET_COMMAND_MODEL);
    }

    public static AdoptPetCommand anEmptyAdoptPetCommand() {
        return new AdoptPetCommand(null, null);
    }

    public static AdoptPetCommand anAdoptPetCommandWithOwnerId(Integer ownerId) {
        return Instancio.of(AdoptPetCommand.class)
                .set(field(AdoptPetCommand::ownerId), ownerId)
                .create();
    }

    public static AdoptPetCommand anAdoptPetCommandWithPetId(Integer petId) {
        return Instancio.of(AdoptPetCommand.class)
                .set(field(AdoptPetCommand::petId), petId)
                .create();
    }

    public static AdoptPetCommand anAdoptPetCommandWithPetIdAndOwnerId(Integer petId, Integer ownerId) {
        return Instancio.of(AdoptPetCommand.class)
                .set(field(AdoptPetCommand::petId), petId)
                .set(field(AdoptPetCommand::ownerId), ownerId)
                .create();
    }

}