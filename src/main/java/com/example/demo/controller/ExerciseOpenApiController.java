package com.example.demo.controller;

import com.example.demo.dto.ExerciseOpenApiResponseDTO;
import com.example.demo.service.ExerciseOpenApiService;
import com.example.demo.service.impl.ExerciseOpenApiServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exerciseopenapi")
public class ExerciseOpenApiController {
    private final ExerciseOpenApiService service;

    public ExerciseOpenApiController(ExerciseOpenApiServiceImpl service) {
        this.service = service;
    }

    @GetMapping("/data")
    public ExerciseOpenApiResponseDTO getData(@RequestParam String key) {
        return service.checkAndQueueRequest(key);
    }
}