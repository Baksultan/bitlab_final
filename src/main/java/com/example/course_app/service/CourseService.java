package com.example.course_app.service;

import com.example.course_app.dto.CourseDTO;
import com.example.course_app.mapper.CourseMapper;
import com.example.course_app.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;


    public List<CourseDTO> getCourses(){
        return courseMapper.toDtoList(courseRepository.findAll());
    }

    public CourseDTO addCourse(CourseDTO course){
        return courseMapper.toDto(courseRepository.save(courseMapper.toModel(course)));
    }

    public CourseDTO getCourse(Long id){
        return courseMapper.toDto(courseRepository.findById(id).orElse(null));
    }

    public CourseDTO updateCourse(CourseDTO course){
        return courseMapper.toDto(courseRepository.save(courseMapper.toModel(course)));
    }

    public void deleteCourse(Long id){
        courseRepository.deleteById(id);
    }

}
