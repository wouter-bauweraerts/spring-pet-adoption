package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.allInts;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.instancio.Model;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mapstruct.factory.Mappers;
import org.springframework.data.util.Pair;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request.AddPetRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;

class PetMapperTest {
    PetMapper mapper = Mappers.getMapper(PetMapper.class);

    @Test
    void map_canHandleNull() {
        assertThat(mapper.map(null)).isNull();
    }

    private static final Model<Pet> PET_MODEL = Instancio.of(Pet.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .generate(field(Pet::getType), gen -> gen.enumOf(PetType.class))
            .toModel();

    private static final Model<AddPetRequest> ADD_PET_REQUEST_MODEL = Instancio.of(AddPetRequest.class)
            .generate(field(AddPetRequest::type), gen -> gen.enumOf(PetType.class).as(Enum::name))
            .toModel();

    @TestFactory
    Stream<DynamicTest> mapFromPetToPetResponse_returnsExpected() {
        return Stream.of(
                        Instancio.create(PET_MODEL),
                        Instancio.of(PET_MODEL).ignore(field(Pet::getName)).ignore(field(Pet::getOwnerId)).create(),
                        Instancio.of(PET_MODEL).ignore(field(Pet::getType)).create()
                ).map(pet -> Pair.of(
                        pet,
                        new PetResponse(
                                pet.getId(),
                                pet.getName(),
                                Optional.ofNullable(pet.getType())
                                        .map(Enum::name)
                                        .orElse(null)
                        )
                ))
                .map(pair -> dynamicTest(
                        "%s maps to %s".formatted(pair.getFirst(), pair.getSecond()),
                        () -> assertThat(mapper.map(pair.getFirst())).isEqualTo(pair.getSecond())
                ));
    }

    @TestFactory
    Stream<DynamicTest> toEntity_mapsToExpected() {
        return Stream.of(
                null,
                Instancio.of(ADD_PET_REQUEST_MODEL).ignore(field(AddPetRequest::name)).create(),
                Instancio.create(ADD_PET_REQUEST_MODEL)
        ).map(req -> dynamicTest(
                "%s is mapped to expected entity",
                () -> {
                    Pet pet = Objects.isNull(req) ? null : new Pet(null, req.name(), PetType.valueOf(req.type()), null);
                    assertThat(mapper.toEnity(req)).isEqualTo(pet);
                }
        ));
    }
}