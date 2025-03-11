package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request.AddPetRequestFixtures.anAddPetRequest;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request.AddPetRequestFixtures.anEmptyAddPetRequest;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetFixtures.aPet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mapstruct.factory.Mappers;
import org.springframework.data.util.Pair;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetFixtureBuilder;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;

class PetMapperTest {
    PetMapper mapper = Mappers.getMapper(PetMapper.class);

    @Test
    void map_canHandleNull() {
        assertThat(mapper.map(null)).isNull();
    }

    @TestFactory
    Stream<DynamicTest> mapFromPetToPetResponse_returnsExpected() {
        return Stream.of(
                        aPet(),
                        PetFixtureBuilder.fixtureBuilder().ignoreName().ignoreOwnerId().build(),
                        PetFixtureBuilder.fixtureBuilder().ignoreType().build()
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
                anEmptyAddPetRequest(),
                anAddPetRequest()
        ).map(req -> dynamicTest(
                "%s is mapped to expected entity",
                () -> {
                    Pet pet = Objects.isNull(req)
                            ? null
                            : new Pet(
                                    null,
                            req.name(),
                            Optional.ofNullable(req.type())
                                    .map(PetType::valueOf)
                                    .orElse(null),
                            null
                    );
                    assertThat(mapper.toEnity(req)).isEqualTo(pet);
                }
        ));
    }
}