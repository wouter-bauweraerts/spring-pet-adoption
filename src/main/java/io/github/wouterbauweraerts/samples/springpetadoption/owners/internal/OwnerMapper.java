package io.github.wouterbauweraerts.samples.springpetadoption.owners.internal;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.ERROR;

import org.mapstruct.Mapper;

import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.internal.domain.Owner;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ERROR)
public interface OwnerMapper {
    OwnerResponse map(Owner owner);
}
