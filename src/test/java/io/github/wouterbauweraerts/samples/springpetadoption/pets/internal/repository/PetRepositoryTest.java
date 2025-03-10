package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository;

import static io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository.PetSpecification.adoptablePetSearchSpecification;
import static net.datafaker.transformations.Field.field;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;
import net.datafaker.Faker;
import net.datafaker.providers.base.BaseFaker;
import net.datafaker.transformations.Schema;

@DataJpaTest
class PetRepositoryTest {
    private static final Faker FAKER = new Faker();
    @Autowired
    PetRepository petRepository;

    @AfterEach
    void tearDown() {
        petRepository.deleteAll();
    }

    @Test
    void findById() {
        Pet pet = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        assertThat(petRepository.findById(pet.getId())).hasValue(pet);
    }

    @Test
    void findById_withNonExistingId_returnsEmpty() {
        assertThat(petRepository.findById(-1)).isEmpty();
    }

    @Test
    void findPetsAvailableForAdoption_returnsExpected() {
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));

        Pet adoptable1 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));
        Pet adoptable2 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));
        Pet adoptable3 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));
        Pet adoptable4 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));
        Pet adoptable5 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));

        assertThat(petRepository.findPetsAvailableForAdoption(Pageable.unpaged())).containsExactlyInAnyOrder(
                adoptable1, adoptable2, adoptable3, adoptable4, adoptable5
        );
    }

    @Test
    void findAllByOwnerId_returnsExpectedPets() {
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        Pet pet1 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> 13)
                )
        ));
        Pet pet2 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> 13)
                )
        ));
        Pet pet3 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> 13)
                )
        ));
        Pet pet4 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> 13)
                )
        ));

        assertThat(petRepository.findAllByOwnerId(13)).containsExactlyInAnyOrder(
                pet1, pet2, pet3, pet4
        );
    }

    @Test
    void deleteAllByOwnerId_deletesExpectedPets() {
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        Pet pet1 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> 13)
                )
        ));
        Pet pet2 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> 13)
                )
        ));
        Pet pet3 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> 13)
                )
        ));
        Pet pet4 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> 13)
                )
        ));

        assertThat(petRepository.findAll()).contains(pet1, pet2, pet3, pet4);

        petRepository.deleteAllByOwnerId(13);

        assertThat(petRepository.findAll()).doesNotContain(pet1, pet2, pet3, pet4);
    }

    @Test
    void findAll_pagedWithSpecification_noSearchParams_returnsExpected() {
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));

        Pet adoptable1 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));
        Pet adoptable2 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));
        Pet adoptable3 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));
        Pet adoptable4 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));
        Pet adoptable5 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));


        Specification<Pet> spec = adoptablePetSearchSpecification(List.of(), List.of());

        assertThat(petRepository.findAll(spec, PageRequest.of(0, 20)))
                .containsExactlyInAnyOrder(
                        adoptable1, adoptable2, adoptable3, adoptable4, adoptable5
                );
    }

    @Test
    void findAll_pagedWithSpecification_namesOnly_returnsExpected() {
        Pet pet = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class)),
                        field("ownerId", () -> FAKER.number().positive())
                )
        ));

        Pet adoptable1 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));
        Pet adoptable2 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));
        Pet adoptable3 = petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));

        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));
        petRepository.save(BaseFaker.populate(
                Pet.class,
                Schema.of(
                        field("name", () -> FAKER.dog().name()),
                        field("type", () -> FAKER.options().option(PetType.class))
                )
        ));

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