package com.example.demo.controller;

import com.example.demo.dto.ExerciseRequestDTO;
import com.example.demo.dto.ExerciseResponseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.service.ExerciseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercises")
@AllArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;


    @PostMapping
    public ResponseEntity<ExerciseResponseDTO> createExercise(@Valid @RequestBody ExerciseRequestDTO exerciseRequestDTO) {
        ExerciseResponseDTO responseDTO = exerciseService.createExercise(exerciseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<Exercise>> getExercises(
            @RequestParam(required = false) String bodyPart) {

        List<Exercise> exercises = exerciseService.getExercises(bodyPart);
        return ResponseEntity.ok(exercises);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponseDTO> updateExercise(
            @PathVariable Long id,
            @Valid @RequestBody ExerciseRequestDTO updatedExercise ) {

        ExerciseResponseDTO exercise = exerciseService.updateExercise(id, updatedExercise);
        return ResponseEntity.ok(exercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }
}