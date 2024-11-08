package com.example.demo.service;

import com.example.demo.dto.ExerciseRequestDTO;
import com.example.demo.dto.ExerciseResponseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.mapper.ExerciseMapper;
import com.example.demo.repository.ExerciseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public List<Exercise> getExercises(String bodyPart) {
        if (bodyPart != null) {
            return exerciseRepository.findByBodyPart(bodyPart);
        } else {
            return exerciseRepository.findAll();
        }
    }

    public Exercise updateExercise(Long id, Exercise updatedExercise) {
        Optional<Exercise> registeredExercise = exerciseRepository.findById(id);
        if (registeredExercise.isPresent()) {
            Exercise exercise = registeredExercise.get();
            exercise.setSets(updatedExercise.getSets());
            exercise.setReps(updatedExercise.getReps());
            exercise.setBodyPart(updatedExercise.getBodyPart());
            exercise.setName(updatedExercise.getName());
            exercise.setDescription(updatedExercise.getDescription());
            return exerciseRepository.save(exercise);
        } else {
            throw new RuntimeException("Exercise not found with id: " + id);
        }
    }

    public void deleteExercise(Long id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Exercise not found with id: " + id);
        }
    }
}