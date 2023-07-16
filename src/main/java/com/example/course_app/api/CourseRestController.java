package com.example.course_app.api;


import com.example.course_app.dto.CourseDTO;
import com.example.course_app.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final CourseService courseService;

    @GetMapping
    public List<CourseDTO> courseList(){
        return courseService.getCourses();
    }

    @GetMapping(value = "{id}")
    public CourseDTO getCourse(@PathVariable(name = "id") Long id){
        return courseService.getCourse(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public CourseDTO addCourse(@RequestBody CourseDTO course){
        return courseService.addCourse(course);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public CourseDTO updateCourse(@RequestBody CourseDTO course){
        return courseService.updateCourse(course);
    }

    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteCourse(@PathVariable(name = "id") Long id){
        courseService.deleteCourse(id);
    }

}
