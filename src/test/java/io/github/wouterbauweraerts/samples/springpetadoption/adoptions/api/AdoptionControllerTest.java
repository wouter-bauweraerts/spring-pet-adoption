package io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import io.github.wouterbauweraerts.samples.springpetadoption.adoptions.api.request.AdoptablePetSearch;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.PetService;

@WebMvcTest(AdoptionController.class)
class AdoptionControllerTest {
    @Autowired
    MockMvcTester mockMvc;

    @MockitoBean
    PetService petService;

    @Captor
    ArgumentCaptor<AdoptablePetSearch> searchCaptor;

    @Test
    void searchAdoptablePetWithoutParametersReturnsOk() {
        assertThat(mockMvc.get().uri("/adoptions/search"))
                .hasStatus(OK);

        verify(petService).searchAdoptablePets(searchCaptor.capture(), any(Pageable.class));

        assertThat(searchCaptor.getValue())
                .returns(List.of(), AdoptablePetSearch::getTypes)
                .returns(List.of(), AdoptablePetSearch::getNames);
    }

    @Test
    void searchAdoptablePetWithoutInvalidType_returnsBadRequest() {
        assertThat(mockMvc.get().uri("/adoptions/search").queryParam("types", "CAR"))
                .hasStatus(BAD_REQUEST);

        verifyNoInteractions(petService);
    }

    @Test
    void searchAdoptablePetWithMultipleTypes_callsWithExpected() {
        assertThat(
                mockMvc.get()
                        .uri("/adoptions/search")
                        .queryParam("types", "DOG", "CAT")
        ).hasStatus(OK);

        verify(petService).searchAdoptablePets(searchCaptor.capture(), any(Pageable.class));

        assertThat(searchCaptor.getValue())
                .returns(List.of("DOG", "CAT"), AdoptablePetSearch::getTypes)
                .returns(List.of(), AdoptablePetSearch::getNames);
    }

    @Test
    void searchAdoptablePetWithMultipleSearchParams_callsWithExpected() {
        assertThat(
                mockMvc.get()
                        .uri("/adoptions/search")
                        .queryParam("types", "DOG", "CAT")
                        .queryParam("names", "Roxy", "Rex")
        ).hasStatus(OK);

        verify(petService).searchAdoptablePets(searchCaptor.capture(), any(Pageable.class));

        assertThat(searchCaptor.getValue())
                .returns(List.of("DOG", "CAT"), AdoptablePetSearch::getTypes)
                .returns(List.of("Roxy", "Rex"), AdoptablePetSearch::getNames);
    }
}