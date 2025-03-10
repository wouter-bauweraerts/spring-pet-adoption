package io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

public class OwnerResponse {
    private int id;
    private String name;
    private Map<String, List<String>> pets;

    public OwnerResponse() {
    }

    public OwnerResponse(int id, String name, Map<String, List<String>> pets) {
        this.id = id;
        this.name = name;
        this.pets = pets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, List<String>> getPets() {
        return pets;
    }

    public void setPets(Map<String, List<String>> pets) {
        this.pets = pets;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OwnerResponse that)) return false;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(pets, that.pets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pets);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OwnerResponse.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("pets=" + pets)
                .toString();
    }

    public OwnerResponse addPets(Map<String, List<String>> pets) {
        this.setPets(pets);
        return this;
    }
}
