package com.example.demo.service.impl;

import com.example.demo.dto.ExerciseRequestDTO;
import com.example.demo.dto.ExerciseResponseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.mapper.ExerciseMapper;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {


    private ExerciseRepository exerciseRepository;

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

    public ExerciseResponseDTO updateExercise(Long id, ExerciseRequestDTO updatedExercise) {
        Optional<Exercise> registeredExercise = exerciseRepository.findById(id);
        if (registeredExercise.isPresent()) {
            Exercise exercise = registeredExercise.get();

            if (updatedExercise.getSetsReq() != 0) exercise.setSets(updatedExercise.getSetsReq());
            if (updatedExercise.getRepsReq() != 0) exercise.setReps(updatedExercise.getRepsReq());
            if (updatedExercise.getBodyPart() != null) exercise.setBodyPart(updatedExercise.getBodyPart());
            if (updatedExercise.getName() != null) exercise.setName(updatedExercise.getName());
            if (updatedExercise.getDescription() != null) exercise.setDescription(updatedExercise.getDescription());

            Exercise savedExercise = exerciseRepository.save(exercise);
            return exerciseMapper.toResponseDTO(savedExercise);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found with id: " + id);
        }
    }

    public void deleteExercise(Long id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Exercise not found with id: " + id);
        }
    }

}