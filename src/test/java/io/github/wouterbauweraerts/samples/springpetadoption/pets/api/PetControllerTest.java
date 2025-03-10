package io.github.wouterbauweraerts.samples.springpetadoption.pets.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.instancio.Select.all;
import static org.instancio.Select.allInts;
import static org.instancio.Select.field;
import static org.instancio.Select.fields;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.instancio.Instancio;
import org.instancio.Model;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.wouterbauweraerts.samples.springpetadoption.pets.PetService;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.request.AddPetRequest;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.api.response.PetResponse;
import io.github.wouterbauweraerts.samples.springpetadoption.pets.internal.domain.PetType;

@WebMvcTest(PetController.class)
class PetControllerTest {
    @Autowired
    MockMvcTester mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    PetService petService;

    private static final Model<PetResponse> PET_RESPONSE_MODEL = Instancio.of(PetResponse.class)
            .generate(allInts(), gen -> gen.ints().min(1))
            .generate(field(PetResponse::type), gen -> gen.enumOf(PetType.class).as(Enum::name))
            .toModel();

    @Test
    void listPetsReturnsExpected() {
        PetResponse pet1 = Instancio.create(PET_RESPONSE_MODEL);
        PetResponse pet2 = Instancio.create(PET_RESPONSE_MODEL);
        PetResponse pet3 = Instancio.create(PET_RESPONSE_MODEL);

        List<PetResponse> pets = List.of(
                pet1,
                pet2,
                pet3
        );

        when(petService.getPets(any(Pageable.class))).thenReturn(new PageImpl<>(pets));

        assertThat(mockMvc.get().uri("/pets"))
                .hasStatus(OK)
                .bodyJson().hasPathSatisfying("$.content.[0].id", value -> value.assertThat().isEqualTo(pet1.id()))
                .hasPathSatisfying("$.content.[0].name", value -> value.assertThat().isEqualTo(pet1.name()))
                .hasPathSatisfying("$.content.[0].type", value -> value.assertThat().isEqualTo(pet1.type()))
                .hasPathSatisfying("$.content.[1].id", value -> value.assertThat().isEqualTo(pet2.id()))
                .hasPathSatisfying("$.content.[1].name", value -> value.assertThat().isEqualTo(pet2.name()))
                .hasPathSatisfying("$.content.[1].type", value -> value.assertThat().isEqualTo(pet2.type()))
                .hasPathSatisfying("$.content.[2].id", value -> value.assertThat().isEqualTo(pet3.id()))
                .hasPathSatisfying("$.content.[2].name", value -> value.assertThat().isEqualTo(pet3.name()))
                .hasPathSatisfying("$.content.[2].type", value -> value.assertThat().isEqualTo(pet3.type()));
    }

    @Test
    void getPetWithNonExistingId_returns404() {
        when(petService.getPet(anyInt())).thenReturn(Optional.empty());

        assertThat(mockMvc.get().uri("/pets/666"))
                .hasStatus(NOT_FOUND);
    }

    @Test
    void getPetWithExistingPet_returnsExpected() {
        PetResponse petResponse = Instancio.create(PET_RESPONSE_MODEL);
        when(petService.getPet(anyInt())).thenReturn(Optional.of(petResponse));

        assertThat(mockMvc.get().uri("/pets/%d".formatted(petResponse.id())))
                .hasStatus(OK)
                .bodyJson()
                .hasPathSatisfying("$.id", value -> value.assertThat().isEqualTo(petResponse.id()))
                .hasPathSatisfying("$.name", value -> value.assertThat().isEqualTo(petResponse.name()))
                .hasPathSatisfying("$.type", value -> value.assertThat().isEqualTo(petResponse.type()));
    }

    @TestFactory
    Stream<DynamicTest> addPetWithInvalidRequest_returnsBadRequestStatus() {
        return Stream.of(
                Instancio.of(AddPetRequest.class)
                        .ignore(fields())
                        .create(),
                Instancio.of(AddPetRequest.class)
                        .ignore(field(AddPetRequest::type))
                        .set(field(AddPetRequest::name), "")
                        .create(),
                Instancio.of(AddPetRequest.class)
                        .ignore(field(AddPetRequest::type))
                        .set(field(AddPetRequest::name), " ")
                        .create(),
                Instancio.of(AddPetRequest.class)
                        .ignore(field(AddPetRequest::type))
                        .set(field(AddPetRequest::name), "Goofy")
                        .create(),
                Instancio.of(AddPetRequest.class)
                        .ignore(field(AddPetRequest::name))
                        .generate(field(AddPetRequest::type), gen -> gen.enumOf(PetType.class).as(Enum::name))
                        .create(),
                Instancio.create(AddPetRequest.class)
        ).map(req -> DynamicTest.dynamicTest(
                "addPet with invalid request %s returns BadRequest".formatted(req),
                () -> {
                    try {
                        assertThat(
                                mockMvc.post()
                                        .uri("/pets")
                                        .contentType(APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(req))
                        ).hasStatus(BAD_REQUEST);
                    } catch (JsonProcessingException e) {
                        fail("Unexpected JsonProcessingException", e);
                    }
                }
        ));
    }

    @Test
    void addPet_withValidRequest_callsServiceToCreateNewPet() throws Exception {
        AddPetRequest goofy = Instancio.of(AddPetRequest.class)
                .generate(field(AddPetRequest::type), gen -> gen.enumOf(PetType.class).as(Enum::name))
                .create();
        PetResponse expectedResponse = Instancio.create(PET_RESPONSE_MODEL);

        when(petService.addPet(any())).thenReturn(expectedResponse);

        assertThat(
                mockMvc.post().uri("/pets")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(goofy))
        ).hasStatus(CREATED)
                .bodyJson()
                .hasPathSatisfying("$.id", value -> value.assertThat().isEqualTo(expectedResponse.id()))
                .hasPathSatisfying("$.name", value -> value.assertThat().isEqualTo(expectedResponse.name()))
                .hasPathSatisfying("$.type", value -> value.assertThat().isEqualTo(expectedResponse.type()));

        verify(petService).addPet(goofy);
    }
}