package io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @Column(name = "pet_id")
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    @Column(name = "pet_name")
    private String name;
    @Column(name = "pet_type")
    @Enumerated(STRING)
    private PetType type;

    @Column(name = "owner")
    private String owner;

    public Pet() {
    }

    public Pet(Integer id, String name, PetType type, String owner) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.owner = owner;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pet pet)) return false;
        return Objects.equals(id, pet.id)
                && Objects.equals(name, pet.name)
                && type == pet.type
                && Objects.equals(owner, pet.owner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, owner);
    }
}
