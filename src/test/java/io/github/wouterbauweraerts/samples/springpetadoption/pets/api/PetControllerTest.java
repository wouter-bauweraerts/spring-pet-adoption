package io.github.wouterbauweraerts.samples.springpetadoption.pets.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

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
                .bodyJson().hasPathSatisfying("$.content.[0].id", value -> assertThat(value).isEqualTo(1))
                .hasPathSatisfying("$.content.[0].name", value -> assertThat(value).isEqualTo("Roxy"))
                .hasPathSatisfying("$.content.[0].type", value -> assertThat(value).isEqualTo("Dog"))
                .hasPathSatisfying("$.content.[1].id", value -> assertThat(value).isEqualTo(2))
                .hasPathSatisfying("$.content.[1].name", value -> assertThat(value).isEqualTo("Rex"))
                .hasPathSatisfying("$.content.[1].type", value -> assertThat(value).isEqualTo("Dog"))
                .hasPathSatisfying("$.content.[2].id", value -> assertThat(value).isEqualTo(3))
                .hasPathSatisfying("$.content.[2].name", value -> assertThat(value).isEqualTo("Filou"))
                .hasPathSatisfying("$.content.[2].type", value -> assertThat(value).isEqualTo("Cat"));
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
}