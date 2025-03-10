package io.github.wouterbauweraerts.samples.springpetadoption.owners.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.mapstruct.factory.Mappers;

import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain.Owner;

class OwnerMapperTest {

    OwnerMapper ownerMapper = Mappers.getMapper(OwnerMapper.class);

    @TestFactory
    Stream<DynamicTest> map() {
        return Stream.of(
                null,
                Instancio.create(Owner.class),
                Instancio.of(Owner.class).ignore(field(Owner::getName)).create()
        ).map(owner -> dynamicTest(
                "%s maps to expected".formatted(owner),
                () -> {
                    OwnerResponse expected = Objects.isNull(owner) ? null : new OwnerResponse(owner.getId(), owner.getName(), Map.of());
                    assertThat(ownerMapper.map(owner)).isEqualTo(expected);
                }
        ));
    }
}