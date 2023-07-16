package com.example.course_app.mapper;


import com.example.course_app.dto.CourseDTO;
import com.example.course_app.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source = "name", target = "courseName")
    CourseDTO toDto(Course course);

    @Mapping(source = "courseName", target = "name")
    Course toModel(CourseDTO courseDTO);

    List<CourseDTO> toDtoList(List<Course> courseList);
    List<Course> toModelList(List<CourseDTO> courseList);

}
