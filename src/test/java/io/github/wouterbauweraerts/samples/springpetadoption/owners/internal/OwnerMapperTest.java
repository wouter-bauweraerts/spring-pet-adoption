package io.github.wouterbauweraerts.samples.springpetadoption.owners.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.mapstruct.factory.Mappers;

import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain.Owner;
import net.datafaker.Faker;

class OwnerMapperTest {
    private static Faker FAKER = new Faker();

    OwnerMapper ownerMapper = Mappers.getMapper(OwnerMapper.class);

    @TestFactory
    Stream<DynamicTest> map() {
        return Stream.of(
                null,
                new Owner(
                        FAKER.number().positive(),
                        FAKER.name().fullName()
                ),
                new Owner(
                        FAKER.number().positive(), null
                )
        ).map(owner -> dynamicTest(
                "%s maps to expected".formatted(owner),
                () -> {
                    OwnerResponse expected = Objects.isNull(owner) ? null : new OwnerResponse(owner.getId(), owner.getName(), Map.of());
                    assertThat(ownerMapper.map(owner)).isEqualTo(expected);
                }
        ));
    }
}