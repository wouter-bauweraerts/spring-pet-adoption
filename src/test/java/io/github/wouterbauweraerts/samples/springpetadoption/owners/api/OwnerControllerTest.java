package io.github.wouterbauweraerts.samples.springpetadoption.owners.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.json.JsonCompareMode.LENIENT;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.wouterbauweraerts.samples.springpetadoption.owners.OwnerService;
import io.github.wouterbauweraerts.samples.springpetadoption.owners.api.response.OwnerResponse;

@WebMvcTest(OwnerController.class)
class OwnerControllerTest {
    @Autowired
    MockMvcTester mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    OwnerService ownerService;

    @Test
    void listOwners() {
        List<OwnerResponse> owners = List.of(
                new OwnerResponse(1, "Wouter", Map.of("DOG", List.of("Roxy"))),
                new OwnerResponse(2, "Josh", Map.of()),
                new OwnerResponse(3, "Alina", Map.of()),
                new OwnerResponse(4, "Venkat", Map.of()),
                new OwnerResponse(5, "Patrick", Map.of())
        );

        when(ownerService.getOwners(any(Pageable.class))).thenReturn(
                new PageImpl<>(owners)
        );

        assertThat(mockMvc.get().uri("/owners"))
                .hasStatus(OK)
                .bodyJson()
                .withResourceLoadClass(null)
                .isEqualTo("all-owners-paged.json", LENIENT);
    }

    @Test
    void getOwnerWithExistingOwner_returnsExpected() throws Exception {
        OwnerResponse ownerResponse = new OwnerResponse(1, "Wouter", Map.of("DOG", List.of("Roxy")));
        when(ownerService.getOwnerById(anyInt())).thenReturn(Optional.of(ownerResponse));

        assertThat(mockMvc.get().uri("/owners/%d".formatted(ownerResponse.getId())))
                .hasStatus(OK)
                .bodyJson()
                .isEqualTo(objectMapper.writeValueAsString(ownerResponse));
    }

    @Test
    void getOwnerWithNonExistingOwner_returns404() {
        when(ownerService.getOwnerById(anyInt())).thenReturn(Optional.empty());

        assertThat(mockMvc.get().uri("/owners/666"))
                .hasStatus(404);
    }
}