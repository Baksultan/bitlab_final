package com.example.course_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDTO {

    private Long id;
    private String courseName;
    private String description;
    private int price;

}