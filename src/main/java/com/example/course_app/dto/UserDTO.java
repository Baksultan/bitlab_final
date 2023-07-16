package com.example.course_app.dto;


import com.example.course_app.model.Course;
import com.example.course_app.model.Permission;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String email;
    private String password;
    private String fullName;
    private List<Course> userCourses;
    private List<Permission> permissions;

}
