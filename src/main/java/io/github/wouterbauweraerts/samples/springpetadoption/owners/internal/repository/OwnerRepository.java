package io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain.Owner;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Integer> {
}
