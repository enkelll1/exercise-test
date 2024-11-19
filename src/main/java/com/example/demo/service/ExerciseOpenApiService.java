package com.example.demo.service;

import com.example.demo.dto.ExerciseOpenApiResponseDTO;

public interface ExerciseOpenApiService {
    ExerciseOpenApiResponseDTO checkAndQueueRequest(String key);
}
