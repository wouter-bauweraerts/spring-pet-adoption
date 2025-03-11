package io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.repository;

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

    Owner owner1, owner2, owner3, owner4, owner5;

    @BeforeEach
    void setUp() {
        owner1 = repository.save(new Owner(null, "Wouter"));
        owner2 = repository.save(new Owner(null, "Frank"));
        owner3 = repository.save(new Owner(null, "Alina"));
        owner4 = repository.save(new Owner(null, "Josh"));
        owner5 = repository.save(new Owner(null, "Venkat"));
    }

    @Test
    void findById_returnsExpected() {
        assertThat(repository.findById(3)).hasValue(owner3);
    }
}