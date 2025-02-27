package io.github.wouterbauweraerts.samples.springpetadoption.pets.service.internal.repository;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.CAT;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.DOG;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.GOAT;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.HAMSTER;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.TURTLE;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository.PetSpecification.adoptablePetSearchSpecification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.test.context.jdbc.Sql;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository.PetRepository;

@DataJpaTest
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/data/PETS.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/data/CLEANUP.sql")
class PetRepositoryTest {
    @Autowired
    PetRepository petRepository;

    @AfterEach
    void tearDown() {
        petRepository.deleteAll();
    }

    @Test
    void contains20Pets() {
        assertThat(petRepository.count()).isEqualTo(20);
    }

    @TestFactory
    Stream<DynamicTest> findAll_withPagination_returnsExpected() {
        return Stream.of(
                Pair.of(
                        PageRequest.of(0, 2),
                        List.of(
                                new Pet(1, "Aloïs", CAT, null),
                                new Pet(2, "Marie-thérèse", HAMSTER, null)
                        )
                ),
                Pair.of(
                        PageRequest.of(0, 5),
                        List.of(
                                new Pet(1, "Aloïs", CAT, null),
                                new Pet(2, "Marie-thérèse", HAMSTER, null),
                                new Pet(3, "Thérèsa", TURTLE, null),
                                new Pet(4, "Amélie", HAMSTER, null),
                                new Pet(5, "Loïca", DOG, null)
                        )
                ),
                Pair.of(
                        PageRequest.of(3, 2),
                        List.of(
                                new Pet(7, "Adélie", HAMSTER, null),
                                new Pet(8, "Mélina", CAT, null)
                        )
                )
        ).map(pair -> dynamicTest(
                "findAll with pagination params %s returns expected elements %s"
                        .formatted(pair.getFirst(), pair.getSecond().stream().map(Pet::getId).toList()),
                () -> assertThat(petRepository.findAll(pair.getFirst())).containsExactlyInAnyOrderElementsOf(pair.getSecond())
        ));
    }

    @TestFactory
    Stream<DynamicTest> findById_returnsExpected() {
        return Stream.of(
                new Pet(1, "Aloïs", CAT, null),
                new Pet(2, "Marie-thérèse", HAMSTER, null),
                new Pet(3, "Thérèsa", TURTLE, null),
                new Pet(14, "Pélagie", CAT, 56),
                new Pet(5, "Loïca", DOG, null)
        ).map(p -> dynamicTest(
                "findById with id %d returns %s".formatted(p.getId(), p),
                () -> assertThat(petRepository.findById(p.getId())).hasValue(p)
        ));
    }

    @Test
    void findById_withNonExistingId_returnsEmpty() {
        assertThat(petRepository.findById(-1)).isEmpty();
    }

    @TestFactory
    Stream<DynamicTest> findPetsAvailableForAdoption_returnsExpected() {
        return Stream.of(
                Pair.of(
                        PageRequest.of(0, 2),
                        List.of(
                                new Pet(1, "Aloïs", CAT, null),
                                new Pet(2, "Marie-thérèse", HAMSTER, null)
                        )
                ),
                Pair.of(
                        PageRequest.of(0, 5),
                        List.of(
                                new Pet(1, "Aloïs", CAT, null),
                                new Pet(2, "Marie-thérèse", HAMSTER, null),
                                new Pet(3, "Thérèsa", TURTLE, null),
                                new Pet(4, "Amélie", HAMSTER, null),
                                new Pet(5, "Loïca", DOG, null)
                        )
                ),
                Pair.of(
                        PageRequest.of(2, 2),
                        List.of(
                                new Pet(5, "Loïca", DOG, null),
                                new Pet(7, "Adélie", HAMSTER, null)
                        )
                )
        ).map(pair -> dynamicTest(
                "findPetsAvailableForAdoption with pagination params %s returns expected elements %s"
                        .formatted(pair.getFirst(), pair.getSecond().stream().map(Pet::getId).toList()),
                () -> assertThat(petRepository.findPetsAvailableForAdoption(pair.getFirst())).containsExactlyInAnyOrderElementsOf(pair.getSecond())
        ));
    }

    @Test
    void findAllByOwnerId_returnsExpectedPets() {
        assertThat(petRepository.findAllByOwnerId(56)).containsExactlyInAnyOrder(
                new Pet(13, "Mélys", GOAT, 56),
                new Pet(14, "Pélagie", CAT, 56)
        );
    }

    @Test
    void deleteAllByOwnerId_deletesExpectedPets() {
        Pet goat = new Pet(13, "Mélys", GOAT, 56);
        Pet cat = new Pet(14, "Pélagie", CAT, 56);

        assertThat(petRepository.findAll()).contains(goat, cat);

        petRepository.deleteAllByOwnerId(56);

        assertThat(petRepository.findAll()).doesNotContain(goat, cat);
    }

    @Test
    void findAll_pagedWithSpecification_noSearchParams_returnsExpected() {
        Specification<Pet> spec = adoptablePetSearchSpecification(List.of(), List.of());

        assertThat(petRepository.findAll(spec, PageRequest.of(0, 20)))
                .containsExactlyInAnyOrder(
                        new Pet(1, "Aloïs", CAT, null),
                        new Pet(2, "Marie-thérèse", HAMSTER, null),
                        new Pet(3, "Thérèsa", TURTLE, null),
                        new Pet(4, "Amélie", HAMSTER, null),
                        new Pet(5, "Loïca", DOG, null),
                        new Pet(7, "Adélie", HAMSTER, null),
                        new Pet(8, "Mélina", CAT, null),
                        new Pet(9, "Noëlla", DOG, null),
                        new Pet(10, "Camélia", DOG, null),
                        new Pet(11, "Neville", TURTLE, null),
                        new Pet(12, "Cécile", DOG, null),
                        new Pet(16, "Yáo", DOG, null),
                        new Pet(17, "Mén", TURTLE, null),
                        new Pet(19, "Anaëlle", CAT, null)
                );
    }

    @Test
    void findAll_pagedWithSpecification_noSearchParams_paginated_returnsExpected() {
        Specification<Pet> spec = adoptablePetSearchSpecification(List.of(), List.of());

        assertThat(petRepository.findAll(spec, PageRequest.of(0, 2)))
                .containsExactlyInAnyOrder(
                        new Pet(1, "Aloïs", CAT, null),
                        new Pet(2, "Marie-thérèse", HAMSTER, null)
                );
    }

    @Test
    void findAll_pagedWithSpecification_namesOnly_returnsExpected() {
        Specification<Pet> spec = adoptablePetSearchSpecification(
                List.of(),
                List.of("Roxy", "Camélia", "Loïca", "Adélie", "Camélia", "Frey")
        );

        assertThat(petRepository.findAll(spec, PageRequest.of(0, 20)))
                .containsExactlyInAnyOrder(
                        new Pet(5, "Loïca", DOG, null),
                        new Pet(7, "Adélie", HAMSTER, null),
                        new Pet(10, "Camélia", DOG, null)
                );
    }

    @Test
    void findAll_pagedWithSpecification_namesAndTypes_returnsExpected() {
        Specification<Pet> spec = adoptablePetSearchSpecification(
                List.of(DOG, HAMSTER),
                List.of("Loïca", "Adélie", "Dà", "Camélia")
        );

        assertThat(petRepository.findAll(spec, PageRequest.of(0, 20)))
                .containsExactlyInAnyOrder(
                        new Pet(5, "Loïca", DOG, null),
                        new Pet(7, "Adélie", HAMSTER, null),
                        new Pet(10, "Camélia", DOG, null)
                );
    }

    @Test
    void findAll_pagedWithSpecification_typesOnly_returnsExpected() {
        Specification<Pet> spec = adoptablePetSearchSpecification(
                List.of(DOG, GOAT),
                List.of()
        );

        assertThat(petRepository.findAll(spec, PageRequest.of(0, 20)))
                .containsExactlyInAnyOrder(
                        new Pet(5, "Loïca", DOG, null),
                        new Pet(9, "Noëlla", DOG, null),
                        new Pet(10, "Camélia", DOG, null),
                        new Pet(12, "Cécile", DOG, null),
                        new Pet(16, "Yáo", DOG, null)
                );
    }
}