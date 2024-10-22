package com.example.demo.controller;

import com.example.demo.dto.ExerciseRequestDTO;
import com.example.demo.dto.ExerciseResponseDTO;
import com.example.demo.service.ExerciseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;

    @PostMapping
    public ResponseEntity<ExerciseResponseDTO> createExercise(@RequestBody ExerciseRequestDTO exerciseRequestDTO) {
        ExerciseResponseDTO responseDTO = exerciseService.createExercise(exerciseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}