package com.example.demo.mapper;

import com.example.demo.dto.ExerciseRequestDTO;
import com.example.demo.entity.Exercise;
import org.mapstruct.Mapper;
import com.example.demo.dto.ExerciseResponseDTO;


@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    ExerciseResponseDTO toResponseDTO(Exercise exercise);

    Exercise toEntity(ExerciseRequestDTO exerciseRequestDTO);
}