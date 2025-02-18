package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request.AddPetRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.Pet;

@Mapper(componentModel = SPRING)
public abstract class PetMapper {
    public abstract PetResponse map(Pet pet);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    public abstract Pet toEnity(AddPetRequest request);
}
