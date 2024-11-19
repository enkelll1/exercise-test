package com.example.demo.service;

import com.example.demo.dto.ExerciseRequestDTO;
import com.example.demo.dto.ExerciseResponseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.mapper.ExerciseMapper;
import com.example.demo.repository.ExerciseRepository;
import com.example.demo.service.impl.ExerciseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExerciseServiceTest {

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private ExerciseMapper exerciseMapper;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createExercise_ShouldReturnSavedExerciseResponseDTO() {
        ExerciseRequestDTO requestDTO = new ExerciseRequestDTO();
        Exercise exercise = new Exercise();
        Exercise savedExercise = new Exercise();
        savedExercise.setId(1L);
        ExerciseResponseDTO responseDTO = new ExerciseResponseDTO();
        responseDTO.setId(1);

        when(exerciseMapper.toEntity(requestDTO)).thenReturn(exercise);
        when(exerciseRepository.save(exercise)).thenReturn(savedExercise);
        when(exerciseMapper.toResponseDTO(savedExercise)).thenReturn(responseDTO);

        ExerciseResponseDTO result = exerciseService.createExercise(requestDTO);

        assertEquals(responseDTO, result);

        verify(exerciseMapper, times(1)).toEntity(requestDTO);
        verify(exerciseRepository, times(1)).save(exercise);
        verify(exerciseMapper, times(1)).toResponseDTO(savedExercise);
    }

    @Test
    void getExercises_ShouldReturnListOfExercises_WhenBodyPartIsNull() {
        Exercise exercise1 = new Exercise();
        Exercise exercise2 = new Exercise();
        List<Exercise> mockExercises = Arrays.asList(exercise1, exercise2);

        when(exerciseRepository.findAll()).thenReturn(mockExercises);

        List<Exercise> result = exerciseService.getExercises(null);

        assertEquals(mockExercises, result);
        verify(exerciseRepository, times(1)).findAll();
    }

    @Test
    void getExercises_ShouldReturnListOfExercises_WhenBodyPartIsProvided() {
        String bodyPart = "arms";
        Exercise exercise = new Exercise();
        exercise.setBodyPart(bodyPart);
        List<Exercise> mockExercises = List.of(exercise);

        when(exerciseRepository.findByBodyPart(bodyPart)).thenReturn(mockExercises);

        List<Exercise> result = exerciseService.getExercises(bodyPart);

        assertEquals(mockExercises, result);
        verify(exerciseRepository, times(1)).findByBodyPart(bodyPart);
    }

    @Test
    void testUpdateExercise_WhenExerciseExists_UpdatesSuccessfully() {
        Long exerciseId = 1L;
        Exercise existingExercise = new Exercise();
        existingExercise.setId(exerciseId);
        existingExercise.setName("Old Exercise");
        existingExercise.setName("Old Exercise");

        ExerciseRequestDTO updateRequest = new ExerciseRequestDTO();
        updateRequest.setName("Updated Exercise");
        updateRequest.setSetsReq(5);
        updateRequest.setRepsReq(10);

        Exercise updatedExercise = new Exercise();
        updatedExercise.setId(exerciseId);
        updatedExercise.setName(updateRequest.getName());
        updatedExercise.setSets(updateRequest.getSetsReq());
        updatedExercise.setReps(updateRequest.getRepsReq());

        ExerciseResponseDTO responseDTO = new ExerciseResponseDTO();
        responseDTO.setId(Math.toIntExact(exerciseId));
        responseDTO.setName(updateRequest.getName());

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.of(existingExercise));
        when(exerciseRepository.save(existingExercise)).thenReturn(updatedExercise);
        when(exerciseMapper.toResponseDTO(updatedExercise)).thenReturn(responseDTO);

        ExerciseResponseDTO result = exerciseService.updateExercise(exerciseId, updateRequest);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getName(), result.getName());

        verify(exerciseRepository).findById(exerciseId);
        verify(exerciseRepository).save(existingExercise);
        verify(exerciseMapper).toResponseDTO(updatedExercise);
    }

    @Test
    void testUpdateExercise_WhenExerciseDoesNotExist_ThrowsException() {
        Long exerciseId = 2L;
        ExerciseRequestDTO updateRequest = new ExerciseRequestDTO();
        updateRequest.setName("Updated Exercise");

        when(exerciseRepository.findById(exerciseId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> exerciseService.updateExercise(exerciseId, updateRequest));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Exercise not found with id: " + exerciseId));

        verify(exerciseRepository).findById(exerciseId);
        verify(exerciseRepository, never()).save(any());
        verify(exerciseMapper, never()).toResponseDTO(any());
    }

    @Test
    void deleteExercise_ShouldDeleteExercise_WhenExerciseExists() {
        Long id = 1L;

        when(exerciseRepository.existsById(id)).thenReturn(true);

        exerciseService.deleteExercise(id);

        verify(exerciseRepository, times(1)).existsById(id);
        verify(exerciseRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteExercise_ShouldThrowException_WhenExerciseNotFound() {
        Long id = 1L;

        when(exerciseRepository.existsById(id)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            exerciseService.deleteExercise(id);
        });

        assertEquals("Exercise not found with id: " + id, exception.getReason());

        verify(exerciseRepository, times(1)).existsById(id);
        verify(exerciseRepository, never()).deleteById(anyLong());
    }
}