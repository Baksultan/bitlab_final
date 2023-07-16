package com.example.course_app.api;


import com.example.course_app.dto.CourseDTO;
import com.example.course_app.dto.UserDTO;
import com.example.course_app.model.PasswordUpdateRequest;
import com.example.course_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<UserDTO> userList() {
        return userService.getUsers();
    }

    @GetMapping(value = "{id}")
    public UserDTO getUser(@PathVariable(name = "id") Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    @PreAuthorize("!isAuthenticated()")
    public ResponseEntity<String> addUser(@RequestBody UserDTO user) {
        return userService.registerUser(user);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public UserDTO updateUser(@RequestBody UserDTO user){
        return userService.updateUser(user);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public UserDTO firsUpdateProfile(@PathVariable(name = "id") Long id, @RequestBody UserDTO userDTO) {
        return userService.updateUser1(userDTO, id);
    }

    @PutMapping("/second-update/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> secondUpdateProfile(@PathVariable(name = "id") Long id, @RequestBody UserDTO userDTO) {
        return userService.updateUser2(userDTO, id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest request) {
        return userService.updatePassword2(request);
    }

    @PostMapping("/enroll/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> enrollCourse(@PathVariable(name = "courseId") Long courseId) {
        return userService.enrollCourserToUser(courseId);
    }

    @PostMapping("/unsub/{courseId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> unsubCourse(@PathVariable(name = "courseId") Long courseId) {
        return userService.unsubCourse(courseId);
    }

    @GetMapping("/my-courses")
    public List<CourseDTO> getUserCourses() {
        return userService.getUserCourses();
    }


    @DeleteMapping(value = "{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public void deleteUser(@PathVariable(name = "id") Long id){
        userService.deleteUser(id);
    }



}
