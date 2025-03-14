package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.request;

import java.util.List;

import io.github.wouterbauweraerts.samples.springpetadoption.common.ValueOfEnum;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;

public class AdoptablePetSearch {
    private List<@ValueOfEnum(enumClass = PetType.class) String> types = List.of();
    private List<String> names = List.of();

    public AdoptablePetSearch() {
    }

    public AdoptablePetSearch(List<String> types, List<String> names) {
        this.types = types;
        this.names = names;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
