package com.example.demo.service;

import com.example.demo.dto.ExerciseRequestDTO;
import com.example.demo.dto.ExerciseResponseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.mapper.ExerciseMapper;
import com.example.demo.repository.ExerciseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ExerciseMapper exerciseMapper;

    @InjectMocks
    private ExerciseService exerciseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createExercise_ShouldReturnSavedExerciseResponseDTO() {
        // Mock request and response
        ExerciseRequestDTO requestDTO = new ExerciseRequestDTO();
        Exercise exercise = new Exercise();
        Exercise savedExercise = new Exercise();
        savedExercise.setId(1L);
        ExerciseResponseDTO responseDTO = new ExerciseResponseDTO();
        responseDTO.setId(1);

        when(exerciseMapper.toEntity(requestDTO)).thenReturn(exercise);
        when(exerciseRepository.save(exercise)).thenReturn(savedExercise);
        when(exerciseMapper.toResponseDTO(savedExercise)).thenReturn(responseDTO);

        // Call service method
        ExerciseResponseDTO result = exerciseService.createExercise(requestDTO);

        // Verify result
        assertEquals(responseDTO, result);

        verify(exerciseMapper, times(1)).toEntity(requestDTO);
        verify(exerciseRepository, times(1)).save(exercise);
        verify(exerciseMapper, times(1)).toResponseDTO(savedExercise);
    }

    @Test
    void getExercises_ShouldReturnListOfExercises_WhenBodyPartIsNull() {
        // Mock response
        Exercise exercise1 = new Exercise();
        Exercise exercise2 = new Exercise();
        List<Exercise> mockExercises = Arrays.asList(exercise1, exercise2);

        when(exerciseRepository.findAll()).thenReturn(mockExercises);

        // Call service method
        List<Exercise> result = exerciseService.getExercises(null);

        // Verify result
        assertEquals(mockExercises, result);
        verify(exerciseRepository, times(1)).findAll();
    }

    @Test
    void getExercises_ShouldReturnListOfExercises_WhenBodyPartIsProvided() {
        // Mock response
        String bodyPart = "arms";
        Exercise exercise = new Exercise();
        exercise.setBodyPart(bodyPart);
        List<Exercise> mockExercises = List.of(exercise);

        when(exerciseRepository.findByBodyPart(bodyPart)).thenReturn(mockExercises);

        // Call service method
        List<Exercise> result = exerciseService.getExercises(bodyPart);

        // Verify result
        assertEquals(mockExercises, result);
        verify(exerciseRepository, times(1)).findByBodyPart(bodyPart);
    }

    @Test
    void updateExercise_ShouldReturnUpdatedExercise() {
        // Mock request and response
        Long id = 1L;
        Exercise existingExercise = new Exercise();
        existingExercise.setId(id);
        Exercise updatedExercise = new Exercise();
        updatedExercise.setSets(3);
        updatedExercise.setReps(10);
        updatedExercise.setBodyPart("legs");
        updatedExercise.setName("Updated Exercise");
        updatedExercise.setDescription("Updated description");

        when(exerciseRepository.findById(id)).thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.save(existingExercise)).thenReturn(existingExercise);

        // Call service method
        Exercise result = exerciseService.updateExercise(id, updatedExercise);

        // Verify result
        assertEquals(existingExercise, result);
        assertEquals("Updated Exercise", result.getName());
        assertEquals(3, result.getSets());
        assertEquals("legs", result.getBodyPart());

        verify(exerciseRepository, times(1)).findById(id);
        verify(exerciseRepository, times(1)).save(existingExercise);
    }

    @Test
    void updateExercise_ShouldThrowException_WhenExerciseNotFound() {
        // Mock response
        Long id = 1L;
        Exercise updatedExercise = new Exercise();

        when(exerciseRepository.findById(id)).thenReturn(Optional.empty());

        // Call service method and verify exception
        Exception exception = assertThrows(RuntimeException.class, () -> {
            exerciseService.updateExercise(id, updatedExercise);
        });

        assertEquals("Exercise not found with id: " + id, exception.getMessage());
        verify(exerciseRepository, times(1)).findById(id);
        verify(exerciseRepository, never()).save(any(Exercise.class));
    }

    @Test
    void deleteExercise_ShouldDeleteExercise_WhenExerciseExists() {
        // Mock response
        Long id = 1L;

        when(exerciseRepository.existsById(id)).thenReturn(true);

        // Call service method
        exerciseService.deleteExercise(id);

        // Verify method call
        verify(exerciseRepository, times(1)).existsById(id);
        verify(exerciseRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteExercise_ShouldThrowException_WhenExerciseNotFound() {
        // Mock response
        Long id = 1L;

        when(exerciseRepository.existsById(id)).thenReturn(false);

        // Call service method and verify exception
        Exception exception = assertThrows(RuntimeException.class, () -> {
            exerciseService.deleteExercise(id);
        });

        assertEquals("Exercise not found with id: " + id, exception.getMessage());
        verify(exerciseRepository, times(1)).existsById(id);
        verify(exerciseRepository, never()).deleteById(id);
    }
}