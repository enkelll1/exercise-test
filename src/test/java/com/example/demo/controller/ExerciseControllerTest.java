package com.example.demo.controller;

import com.example.demo.dto.ExerciseRequestDTO;
import com.example.demo.dto.ExerciseResponseDTO;
import com.example.demo.entity.Exercise;
import com.example.demo.service.ExerciseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExerciseControllerTest {

    @Mock
    private ExerciseService exerciseService;

    @InjectMocks
    private ExerciseController exerciseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(exerciseController).build();
    }

    @Test
    void createExercise_ShouldReturnCreatedResponse() {
        ExerciseRequestDTO requestDTO = new ExerciseRequestDTO();
        ExerciseResponseDTO responseDTO = new ExerciseResponseDTO();
        responseDTO.setId(1);

        when(exerciseService.createExercise(any(ExerciseRequestDTO.class))).thenReturn(responseDTO);

        ResponseEntity<ExerciseResponseDTO> response = exerciseController.createExercise(requestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(responseDTO, response.getBody());

        verify(exerciseService, times(1)).createExercise(any(ExerciseRequestDTO.class));
    }

    @Test
    void getExercises_ShouldReturnListOfExercises() {
        Exercise exercise1 = new Exercise();
        exercise1.setId(1L);
        Exercise exercise2 = new Exercise();
        exercise2.setId(2L);

        List<Exercise> mockExerciseList = Arrays.asList(exercise1, exercise2);
        when(exerciseService.getExercises(null)).thenReturn(mockExerciseList);

        ResponseEntity<List<Exercise>> response = exerciseController.getExercises(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockExerciseList, response.getBody());

        verify(exerciseService, times(1)).getExercises(null);
    }

    @Test
    void updateExercise_ShouldReturnUpdatedExercise() {
        Long id = 1L;
        Exercise updatedExercise = new Exercise();
        updatedExercise.setId(id);
        updatedExercise.setName("Updated Exercise");

        when(exerciseService.updateExercise(id, updatedExercise)).thenReturn(updatedExercise);

        ResponseEntity<Exercise> response = exerciseController.updateExercise(id, updatedExercise);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedExercise, response.getBody());

        verify(exerciseService, times(1)).updateExercise(id, updatedExercise);
    }

    @Test
    void deleteExercise_ShouldReturnNoContent() {
        Long id = 1L;

        ResponseEntity<Void> response = exerciseController.deleteExercise(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(exerciseService, times(1)).deleteExercise(id);
    }
}