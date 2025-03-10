package io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.repository;

import static io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain.OwnerFixtures.anUnpersistedOwner;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain.Owner;

@DataJpaTest
class OwnerRepositoryTest {

    @Autowired
    OwnerRepository repository;

    private Owner owner;

    @BeforeEach
    void setUp() {
        owner = repository.save(anUnpersistedOwner());
    }

    @Test
    void findById_returnsExpected() {
        assertThat(repository.findById(owner.getId())).hasValueSatisfying(
                o -> assertThat(o).returns(owner.getId(), Owner::getId)
                        .returns(owner.getName(), Owner::getName)
        );
    }
}