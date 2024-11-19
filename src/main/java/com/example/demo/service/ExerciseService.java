package com.example.demo.service;

import com.example.demo.dto.ExerciseRequestDTO;
import com.example.demo.dto.ExerciseResponseDTO;
import com.example.demo.entity.Exercise;

import java.util.List;

public interface ExerciseService {
    ExerciseResponseDTO createExercise(ExerciseRequestDTO exerciseRequestDTO);
    List<Exercise> getExercises(String bodyPart);
    ExerciseResponseDTO updateExercise(Long id, ExerciseRequestDTO updatedExercise);
    void deleteExercise(Long id);
}
