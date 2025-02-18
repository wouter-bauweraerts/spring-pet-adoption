package io.github.wouterbauweraerts.samples.springpetadoption.owners.api.request;

import jakarta.validation.constraints.NotBlank;

public record AddOwnerRequest(
        @NotBlank String name
) {
}
