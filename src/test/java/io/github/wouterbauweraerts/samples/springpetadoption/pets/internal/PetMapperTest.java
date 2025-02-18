package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.DOG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mapstruct.factory.Mappers;
import org.springframework.data.util.Pair;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;

class PetMapperTest {
    PetMapper mapper = Mappers.getMapper(PetMapper.class);

    @Test
    void map_canHandleNull() {
        assertThat(mapper.map(null)).isNull();
    }

    @TestFactory
    Stream<DynamicTest> mapFromPetToPetResponse_returnsExpected() {
        return Stream.of(
                Pair.of(
                        new Pet(1, "Roxy", DOG),
                        new PetResponse(1, "Roxy", DOG.name())
                ),
                Pair.of(
                        new Pet(1, null, DOG),
                        new PetResponse(1, null, DOG.name())
                ),
                Pair.of(
                        new Pet(1, "Roxy", null),
                        new PetResponse(1, "Roxy", null)
                )
        ).map(pair -> dynamicTest(
                "%s maps to %s".formatted(pair.getFirst(), pair.getSecond()),
                () -> assertThat(mapper.map(pair.getFirst())).isEqualTo(pair.getSecond())
        ));
    }
}