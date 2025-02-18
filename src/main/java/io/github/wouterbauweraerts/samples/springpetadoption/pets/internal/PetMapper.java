package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.Mapper;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;

@Mapper(componentModel = SPRING)
public abstract class PetMapper {
    public abstract PetResponse map(Pet pet);
}
