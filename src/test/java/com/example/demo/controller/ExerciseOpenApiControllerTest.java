package com.example.demo.controller;


import com.example.demo.dto.ExerciseOpenApiResponseDTO;
import com.example.demo.service.impl.ExerciseOpenApiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ExerciseOpenApiControllerTest {

    @Mock
    private ExerciseOpenApiServiceImpl service;

    @InjectMocks
    private ExerciseOpenApiController controller;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void getData_ShouldReturnDataFromService() {
        String key = "sampleKey";
        ExerciseOpenApiResponseDTO mockResponse = new ExerciseOpenApiResponseDTO().message("mock response data");

        when(service.checkAndQueueRequest(anyString())).thenReturn(mockResponse);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/exerciseopenapi/data")
                        .queryParam("key", key)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ExerciseOpenApiResponseDTO.class)
                .isEqualTo(mockResponse);
    }
}