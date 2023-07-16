package com.example.course_app.service;

import com.example.course_app.dto.CourseDTO;
import com.example.course_app.dto.UserDTO;
import com.example.course_app.mapper.CourseMapper;
import com.example.course_app.mapper.UserMapper;
import com.example.course_app.model.Course;
import com.example.course_app.model.PasswordUpdateRequest;
import com.example.course_app.model.Permission;
import com.example.course_app.model.User;
import com.example.course_app.repository.PermissionRepository;
import com.example.course_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("User Not found");
        }
    }

    public List<UserDTO> getUsers() {
        return userMapper.toDtoList(userRepository.findAll());
    }

    public UserDTO getUser(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElse(new User()));
    }

    public UserDTO addUser(UserDTO user) {
        User checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userMapper.toDto(userRepository.save(userMapper.toModel(user)));
        }
        return null;
    }

    public ResponseEntity<String> registerUser(UserDTO user) {

            User newUser = userMapper.toModel(user);
            addUser2(newUser);
            if (newUser != null) {
                return ResponseEntity.ok("User registered successfully!");
            } else {
                return ResponseEntity.badRequest().body("Email is already registered");
            }

    }

    public ResponseEntity<String> enrollCourserToUser(Long courseId) {
        User user = getCurrentSessionUser();
        if (user != null) {

            for (Course c:user.getUserCourses()) {
                if (c.getId().equals(courseId)) {
                    return ResponseEntity.badRequest().body("User already subscribed!");
                }
            }

            CourseDTO course = courseService.getCourse(courseId);
            if (course != null) {
                user.getUserCourses().add(courseMapper.toModel(course));
                updateUser(userMapper.toDto(user));
                return ResponseEntity.ok("Course enrolled successfully!");
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }
    }

    public ResponseEntity<String> unsubCourse(Long courseId) {
        User user = getCurrentSessionUser();
        boolean success = false;

        if (user != null) {
            List<Course> userCourses = user.getUserCourses();
            Course courseToRemove = null;

            for (Course course : userCourses) {
                if (course.getId().equals(courseId)) {
                    courseToRemove = course;
                    break;
                }
            }

            if (courseToRemove != null) {
                userCourses.remove(courseToRemove);
                updateUser(userMapper.toDto(user));
                success = true;
            }
        }

        if (success) {
            return ResponseEntity.ok("Course unsubscribed successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to unsubscribe from the course");
        }
    }

    public List<CourseDTO> getUserCourses() {
        User currentUser = getCurrentSessionUser();
        return courseMapper.toDtoList(currentUser.getUserCourses());
    }


    public UserDTO updateUser(UserDTO user) {
        return userMapper.toDto(userRepository.save(userMapper.toModel(user)));
    }

    public UserDTO updateUser1(UserDTO userDTO, Long id) {
        if (getCurrentSessionUser().getId() == id) {
            UserDTO user =  getUser(id);
            user.setEmail(userDTO.getEmail());
            user.setFullName(userDTO.getFullName());
            return updateUser(user);
        } else {
            return new UserDTO();
        }
    }

    public ResponseEntity<?> updateUser2(UserDTO updatedUser, Long id) {
        User existingUser = userMapper.toModel(getUser(id));

        existingUser.setFullName(updatedUser.getFullName());
        existingUser.setEmail(updatedUser.getEmail());

        existingUser.getPermissions().clear();

        for (Permission permissionDTO : updatedUser.getPermissions()) {
            Permission existingPermission = permissionRepository.findById(permissionDTO.getId())
                    .orElseThrow(() -> new RuntimeException("Permission not found"));
            existingUser.getPermissions().add(existingPermission);
        }

        User savedUser = userRepository.save(existingUser);
        UserDTO savedUserDTO = userMapper.toDto(savedUser);
        return ResponseEntity.ok(savedUserDTO);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User addUser2(User user) {
        User checkUser = userRepository.findByEmail(user.getEmail());
        if (checkUser == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        return null;
    }

    public UserDTO updatePassword(String oldPassword, String newPassword ) {

        User currentUser = getCurrentSessionUser();
        if(passwordEncoder.matches(oldPassword, currentUser.getPassword())){
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            return userMapper.toDto(userRepository.save(currentUser));
        }
        return null;

    }

    public ResponseEntity<String> updatePassword2(PasswordUpdateRequest request) {
        String oldPassword = request.getOldPassword();
        String newPassword = request.getNewPassword();

        User user = getCurrentSessionUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        UserDTO updatedUser = updatePassword(oldPassword, newPassword);
        if (updatedUser != null) {
            return ResponseEntity.ok("Password updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid old password");
        }
    }

    public User getCurrentSessionUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User user = (User) authentication.getPrincipal();
            if (user != null) {
                return user;
            }
        }
        return null;
    }

}
