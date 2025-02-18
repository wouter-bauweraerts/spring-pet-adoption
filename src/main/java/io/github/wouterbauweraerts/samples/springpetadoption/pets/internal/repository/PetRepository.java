package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
    @Query("SELECT p FROM Pet p WHERE p.ownerId IS NULL")
    Page<Pet> findPetsAvailableForAdoption(Pageable pageable);
}
