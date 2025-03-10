package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository.PetSpecification.adoptablePetSearchSpecification;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

import java.util.List;

import org.instancio.Instancio;
import org.instancio.Model;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;

@DataJpaTest
class PetRepositoryTest {
    @Autowired
    PetRepository petRepository;

    private static final Model<Pet> JPA_PET_MODEL = Instancio.of(Pet.class)
            .ignore(field(Pet::getId))
            .generate(field(Pet::getOwnerId), gen -> gen.ints().min(1))
            .generate(field(Pet::getType), gen -> gen.enumOf(PetType.class))
            .toModel();

    private static final Model<Pet> JPA_ADOPTABLE_PET_MODEL = Instancio.of(JPA_PET_MODEL)
            .ignore(field(Pet::getOwnerId))
            .toModel();

    @AfterEach
    void tearDown() {
        petRepository.deleteAll();
    }

    @Test
    void findById() {
        Pet pet = petRepository.save(Instancio.create(JPA_PET_MODEL));
        assertThat(petRepository.findById(pet.getId())).hasValue(pet);
    }

    @Test
    void findById_withNonExistingId_returnsEmpty() {
        assertThat(petRepository.findById(-1)).isEmpty();
    }

    @Test
    void findPetsAvailableForAdoption_returnsExpected() {
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));

        Pet adoptable1 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));
        Pet adoptable2 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));
        Pet adoptable3 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));
        Pet adoptable4 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));
        Pet adoptable5 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));

        assertThat(petRepository.findPetsAvailableForAdoption(Pageable.unpaged())).containsExactlyInAnyOrder(
                adoptable1, adoptable2, adoptable3, adoptable4, adoptable5
        );
    }

    @Test
    void findAllByOwnerId_returnsExpectedPets() {
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        Pet pet1 = petRepository.save(Instancio.of(JPA_PET_MODEL).set(field(Pet::getOwnerId), 13).create());
        Pet pet2 = petRepository.save(Instancio.of(JPA_PET_MODEL).set(field(Pet::getOwnerId), 13).create());
        Pet pet3 = petRepository.save(Instancio.of(JPA_PET_MODEL).set(field(Pet::getOwnerId), 13).create());
        Pet pet4 = petRepository.save(Instancio.of(JPA_PET_MODEL).set(field(Pet::getOwnerId), 13).create());

        assertThat(petRepository.findAllByOwnerId(13)).containsExactlyInAnyOrder(
                pet1, pet2, pet3, pet4
        );
    }

    @Test
    void deleteAllByOwnerId_deletesExpectedPets() {
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        Pet pet1 = petRepository.save(Instancio.of(JPA_PET_MODEL).set(field(Pet::getOwnerId), 13).create());
        Pet pet2 = petRepository.save(Instancio.of(JPA_PET_MODEL).set(field(Pet::getOwnerId), 13).create());
        Pet pet3 = petRepository.save(Instancio.of(JPA_PET_MODEL).set(field(Pet::getOwnerId), 13).create());
        Pet pet4 = petRepository.save(Instancio.of(JPA_PET_MODEL).set(field(Pet::getOwnerId), 13).create());

        assertThat(petRepository.findAll()).contains(pet1, pet2, pet3, pet4);

        petRepository.deleteAllByOwnerId(13);

        assertThat(petRepository.findAll()).doesNotContain(pet1, pet2, pet3, pet4);
    }

    @Test
    void findAll_pagedWithSpecification_noSearchParams_returnsExpected() {
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));

        Pet adoptable1 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));
        Pet adoptable2 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));
        Pet adoptable3 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));
        Pet adoptable4 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));
        Pet adoptable5 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));


        Specification<Pet> spec = adoptablePetSearchSpecification(List.of(), List.of());

        assertThat(petRepository.findAll(spec, PageRequest.of(0, 20)))
                .containsExactlyInAnyOrder(
                        adoptable1, adoptable2, adoptable3, adoptable4, adoptable5
                );
    }

    @Test
    void findAll_pagedWithSpecification_namesOnly_returnsExpected() {
        Pet pet = petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));
        petRepository.save(Instancio.create(JPA_PET_MODEL));

        Pet adoptable1 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));
        Pet adoptable2 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));
        Pet adoptable3 = petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));

        petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));
        petRepository.save(Instancio.create(JPA_ADOPTABLE_PET_MODEL));

        Specification<Pet> spec = adoptablePetSearchSpecification(
                List.of(),
                List.of(pet.getName(), adoptable1.getName(), adoptable2.getName(), adoptable3.getName())
        );

        assertThat(petRepository.findAll(spec, PageRequest.of(0, 20)))
                .containsExactlyInAnyOrder(
                        adoptable1,
                        adoptable2,
                        adoptable3
                );
    }
}