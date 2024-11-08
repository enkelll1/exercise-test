package com.example.demo.mapper;

import com.example.demo.dto.ExerciseRequestDTO;
import com.example.demo.entity.Exercise;
import org.mapstruct.Mapper;
import com.example.demo.dto.ExerciseResponseDTO;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ExerciseMapper {

//    @Mapping(source = "sets", target = "sets")
//    @Mapping(source = "reps", target = "reps")
//    @Mapping(source = "bodyPart", target = "bodyPart")
//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "description", target = "description")
    ExerciseResponseDTO toResponseDTO(Exercise exercise);

//    @Mapping(source = "sets", target = "sets")
//    @Mapping(source = "reps", target = "reps")
//    @Mapping(source = "bodyPart", target = "bodyPart")
//    @Mapping(source = "name", target = "name")
//    @Mapping(source = "description", target = "description")
    Exercise toEntity(ExerciseRequestDTO exerciseRequestDTO);
}