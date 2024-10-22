package com.example.demo.service;

import com.example.demo.dto.ExerciseRequestDTO;
import com.example.demo.dto.ExerciseResponseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.mapper.ExerciseMapper;
import com.example.demo.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExerciseService {


    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseMapper exerciseMapper;

    public ExerciseResponseDTO createExercise(ExerciseRequestDTO exerciseRequestDTO) {
        Exercise exercise = exerciseMapper.toEntity(exerciseRequestDTO);
        Exercise savedExercise = exerciseRepository.save(exercise);
        return exerciseMapper.toResponseDTO(savedExercise);
    }
}