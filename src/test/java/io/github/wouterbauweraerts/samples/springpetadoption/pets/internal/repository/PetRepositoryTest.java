package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.CAT;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.DOG;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.GOAT;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.HAMSTER;
import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType.TURTLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;

@DataJpaTest
class PetRepositoryTest {
    @Autowired
    PetRepository petRepository;

    Pet pet1, pet2, pet3, pet4, pet5, pet6, pet7, pet8, pet9, pet10;

    @BeforeEach
    void setUp() {
        pet1 = petRepository.save(new Pet(null, "Roxy", DOG, "Wouter"));
        pet2 = petRepository.save(new Pet(null, "Rex", DOG, null));
        pet3 = petRepository.save(new Pet(null, "Filou", CAT, null));
        pet4 = petRepository.save(new Pet(null, "Bella", CAT, null));
        pet5 = petRepository.save(new Pet(null, "Luna", HAMSTER, null));
        pet6 = petRepository.save(new Pet(null, "Hammy", HAMSTER, null));
        pet7 = petRepository.save(new Pet(null, "Michelangelo", TURTLE, null));
        pet8 = petRepository.save(new Pet(null, "Donatello", TURTLE, null));
        pet9 = petRepository.save(new Pet(null, "Leonardo", GOAT, null));
        pet10 = petRepository.save(new Pet(null, "Raphael", GOAT, null));
    }

    @AfterEach
    void tearDown() {
        petRepository.deleteAll();
    }

    @Test
    void findAll_unpaged_returnsAll() {
        assertThat(petRepository.findAll(Pageable.unpaged()))
                .containsExactlyInAnyOrder(
                        pet1, pet2, pet3, pet4, pet5, pet6, pet7, pet8, pet9, pet10
                );
    }

    @TestFactory
    Stream<DynamicTest> findAll_withPagination_returnsExpected() {
        return Stream.of(
                Pair.of(
                        PageRequest.of(0, 2),
                        List.of(pet1, pet2)
                ),
                Pair.of(
                        PageRequest.of(0, 5),
                        List.of(pet1, pet2, pet3, pet4, pet5)
                ),
                Pair.of(
                        PageRequest.of(0, 20),
                        List.of(pet1, pet2, pet3, pet4, pet5, pet6, pet7, pet8, pet9, pet10)
                ),
                Pair.of(
                        PageRequest.of(3, 2),
                        List.of(pet7, pet8)
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
                pet1, pet2, pet3, pet4, pet5, pet6
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
                        List.of(pet2, pet3)
                ),
                Pair.of(
                        PageRequest.of(0, 5),
                        List.of(pet2, pet3, pet4, pet5, pet6)
                ),
                Pair.of(
                        PageRequest.of(0, 20),
                        List.of(pet2, pet3, pet4, pet5, pet6, pet7, pet8, pet9, pet10)
                ),
                Pair.of(
                        PageRequest.of(3, 2),
                        List.of(pet8, pet9)
                )
        ).map(pair -> dynamicTest(
                "findPetsAvailableForAdoption with pagination params %s returns expected elements %s"
                        .formatted(pair.getFirst(), pair.getSecond().stream().map(Pet::getId).toList()),
                () -> assertThat(petRepository.findPetsAvailableForAdoption(pair.getFirst())).containsExactlyInAnyOrderElementsOf(pair.getSecond())
        ));
    }
}