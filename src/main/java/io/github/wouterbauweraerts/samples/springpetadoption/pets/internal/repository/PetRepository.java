package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {
}
