package io.github.wouterbauweraerts.samples.springpetadoption.pets.api;

import static net.datafaker.transformations.Field.field;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
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
import net.datafaker.Faker;
import net.datafaker.providers.base.BaseFaker;
import net.datafaker.transformations.Schema;

@WebMvcTest(PetController.class)
class PetControllerTest {
    private static final Faker FAKER = new Faker();

    @Autowired
    MockMvcTester mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    PetService petService;

    @Test
    void listPetsReturnsExpected() {
        PetResponse pet1 = BaseFaker.populate(PetResponse.class, Schema.<Object, Object>of(
                field("id", () -> FAKER.number().positive()),
                field("name", () -> FAKER.animal().name()),
                field("type", () -> FAKER.options().option(PetType.class).name())
        ));
        PetResponse pet2 = BaseFaker.populate(PetResponse.class, Schema.<Object, Object>of(
                field("id", () -> FAKER.number().positive()),
                field("name", () -> FAKER.animal().name()),
                field("type", () -> FAKER.options().option(PetType.class).name())
        ));
        PetResponse pet3 = BaseFaker.populate(PetResponse.class, Schema.<Object, Object>of(
                field("id", () -> FAKER.number().positive()),
                field("name", () -> FAKER.animal().name()),
                field("type", () -> FAKER.options().option(PetType.class).name())
        ));
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
        PetResponse petResponse = BaseFaker.populate(PetResponse.class, Schema.<Object, Object>of(
                field("id", () -> FAKER.number().positive()),
                field("name", () -> FAKER.animal().name()),
                field("type", () -> FAKER.options().option(PetType.class).name())
        ));
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
                new AddPetRequest(null, null),
                new AddPetRequest("", null),
                new AddPetRequest(" ", null),
                new AddPetRequest("Goofy", null),
                new AddPetRequest(null, FAKER.options().option(PetType.class).name()),
                new AddPetRequest("Mickey", "MOUSE")
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
        AddPetRequest goofy = BaseFaker.populate(AddPetRequest.class, Schema.of(
                field("name", () -> FAKER.dog().name()),
                field("type", () -> FAKER.options().option(PetType.class).name())
        ));
        PetResponse expectedResponse = BaseFaker.populate(PetResponse.class, Schema.<Object, Object>of(
                field("id", () -> FAKER.number().positive()),
                field("name", () -> FAKER.animal().name()),
                field("type", () -> FAKER.options().option(PetType.class).name())
        ));

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