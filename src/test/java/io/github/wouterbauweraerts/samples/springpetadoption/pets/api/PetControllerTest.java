package io.github.wouterbauweraerts.samples.springpetadoption.pets.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.json.JsonCompareMode;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.PetService;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;

@WebMvcTest(PetController.class)
class PetControllerTest {
    @Autowired
    MockMvcTester mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    PetService petService;

    @Test
    void listPetsReturnsExpected() {
        List<PetResponse> pets = List.of(
                new PetResponse(1, "Roxy", "Dog"),
                new PetResponse(2, "Rex", "Dog"),
                new PetResponse(3, "Filou", "Cat")
        );

        when(petService.getPets(any(Pageable.class))).thenReturn(new PageImpl<>(pets));

        assertThat(mockMvc.get().uri("/pets"))
                .hasStatus(OK)
                .bodyJson().hasPathSatisfying("$.content.[0].id", value -> value.assertThat().isEqualTo(1))
                .hasPathSatisfying("$.content.[0].name", value -> value.assertThat().isEqualTo("Roxy"))
                .hasPathSatisfying("$.content.[0].type", value -> value.assertThat().isEqualTo("Dog"))
                .hasPathSatisfying("$.content.[1].id", value -> value.assertThat().isEqualTo(2))
                .hasPathSatisfying("$.content.[1].name", value -> value.assertThat().isEqualTo("Rex"))
                .hasPathSatisfying("$.content.[1].type", value -> value.assertThat().isEqualTo("Dog"))
                .hasPathSatisfying("$.content.[2].id", value -> value.assertThat().isEqualTo(3))
                .hasPathSatisfying("$.content.[2].name", value -> value.assertThat().isEqualTo("Filou"))
                .hasPathSatisfying("$.content.[2].type", value -> value.assertThat().isEqualTo("Cat"));
    }

    @Test
    void listPetsReturnsExpected_fromResource() {
        List<PetResponse> pets = List.of(
                new PetResponse(1, "Roxy", "Dog"),
                new PetResponse(2, "Rex", "Dog"),
                new PetResponse(3, "Filou", "Cat")
        );

        when(petService.getPets(any(Pageable.class))).thenReturn(new PageImpl<>(pets));

        assertThat(mockMvc.get().uri("/pets"))
                .hasStatus(OK)
                .bodyJson()
                .withResourceLoadClass(null)
                .isEqualTo("all-pets-paged.json", JsonCompareMode.LENIENT);
    }

    @Test
    void getPetWithNonExistingId_returns404() {
        when(petService.getPet(anyInt())).thenReturn(Optional.empty());

        assertThat(mockMvc.get().uri("/pets/666"))
                .hasStatus(NOT_FOUND);
    }

    @Test
    void getPetWithExistingPet_returnsExpected() {
        PetResponse petResponse = new PetResponse(1, "Roxy", "Dog");
        when(petService.getPet(anyInt())).thenReturn(Optional.of(petResponse));

        assertThat(mockMvc.get().uri("/pets/%d".formatted(petResponse.id())))
                .hasStatus(OK)
                .bodyJson()
                .hasPathSatisfying("$.id", value -> value.assertThat().isEqualTo(petResponse.id()))
                .hasPathSatisfying("$.name", value -> value.assertThat().isEqualTo(petResponse.name()))
                .hasPathSatisfying("$.type", value -> value.assertThat().isEqualTo(petResponse.type()));
    }

    @Test
    void findAvailablePetsReturnsExpected_fromResource() {
        List<PetResponse> pets = List.of(
                new PetResponse(2, "Rex", "Dog"),
                new PetResponse(3, "Filou", "Cat")
        );

        when(petService.getPetsAvailableForAdoption(any(Pageable.class))).thenReturn(new PageImpl<>(pets));

        assertThat(mockMvc.get().uri("/pets/available-for-adoption"))
                .hasStatus(OK)
                .bodyJson()
                .withResourceLoadClass(null)
                .isEqualTo("all-available-pets-paged.json", JsonCompareMode.LENIENT);
    }
}