package com.example.demo.service;

import com.example.demo.config.MessagingConfig;
import com.example.demo.dto.ExerciseOpenApiResponseDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

@Service
public class ExerciseOpenApiService {

    private final StringRedisTemplate stringRedisTemplate;

    private final RabbitTemplate rabbitTemplate;
    private final WebClient webClient;

    private String baseUrl;


    private final String headerName;


    private final String headerValue;

    public ExerciseOpenApiService(
            @Value("${api.base.url}") String baseUrl,
            @Value("${custom.header.name}") String headerName,
            @Value("${custom.header.value}") String headerValue,
            StringRedisTemplate stringRedisTemplate,
            RabbitTemplate rabbitTemplate,
            WebClient.Builder webClientBuilder) {
        this.headerName = headerName;
        this.headerValue = headerValue;
        this.rabbitTemplate = rabbitTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
        this.baseUrl = baseUrl;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public ExerciseOpenApiResponseDTO checkAndQueueRequest(String key) {
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            String massage = queueRequest(key);
            return new ExerciseOpenApiResponseDTO().message(massage);
        } else {
            return new ExerciseOpenApiResponseDTO().message(key);
        }
    }

    private String queueRequest(String key) {
        rabbitTemplate.convertAndSend(MessagingConfig.REQUEST_QUEUE, key);
        return "Request queued for key: " + key;
    }

    @RabbitListener(queues = MessagingConfig.REQUEST_QUEUE)
    public void makeApiRequest(String key) {
        String response = webClient.get()
                .uri("/exercises?muscle=" + key)
                .header(headerName, headerValue)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (response != null) {
            rabbitTemplate.convertAndSend(MessagingConfig.RESPONSE_QUEUE, response);
        }
    }

    @RabbitListener(queues = MessagingConfig.RESPONSE_QUEUE)
    public void saveResponseToRedis(String response) {
        System.out.println(response);
        stringRedisTemplate.opsForValue()
                .set("key", response, Duration.ofHours(2));
    }
}