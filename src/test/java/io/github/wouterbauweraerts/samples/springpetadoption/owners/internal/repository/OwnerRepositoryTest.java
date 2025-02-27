package io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain.Owner;

@DataJpaTest
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "classpath:data/OWNERS.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "classpath:data/CLEANUP.sql")
class OwnerRepositoryTest {
    @Autowired
    OwnerRepository repository;


    @Test
    void findById_returnsExpected() {
        assertThat(repository.findById(3)).hasValueSatisfying(
                owner -> assertThat(owner).returns(3, Owner::getId)
                        .returns("Nate", Owner::getName)
        );
    }
}