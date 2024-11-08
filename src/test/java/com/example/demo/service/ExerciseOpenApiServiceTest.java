package com.example.demo.service;

import com.example.demo.dto.ExerciseOpenApiResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class ExerciseOpenApiServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private ExerciseOpenApiService exerciseOpenApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock WebClient.Builder behavior
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    @Test
    void testCheckAndQueueRequest_KeyExistsInRedis() {
        String key = "existingKey";
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn("cachedValue");

        ExerciseOpenApiResponseDTO responseDTO = exerciseOpenApiService.checkAndQueueRequest(key);

        assertEquals("Request queued for key: existingKey", responseDTO.getMessage());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString());
    }

    @Test
    void testCheckAndQueueRequest_KeyDoesNotExistInRedis() {
        String key = "newKey";
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(key)).thenReturn(null);

        ExerciseOpenApiResponseDTO responseDTO = exerciseOpenApiService.checkAndQueueRequest(key);

        assertEquals("Request queued for key: newKey", responseDTO.getMessage());
        verify(rabbitTemplate,never()).convertAndSend("request_queue", key);
    }

}