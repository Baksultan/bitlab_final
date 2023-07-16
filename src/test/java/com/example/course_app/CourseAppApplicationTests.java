package com.example.course_app;

import com.example.course_app.dto.CourseDTO;
import com.example.course_app.dto.UserDTO;
import com.example.course_app.mapper.UserMapper;
import com.example.course_app.model.User;
import com.example.course_app.repository.UserRepository;
import com.example.course_app.service.CourseService;
import com.example.course_app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class CourseAppApplicationTests {

	@Mock
	private UserRepository userRepository;

	@Mock
	private UserMapper userMapper;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private CourseService courseService;

	@InjectMocks
	private UserService userService;


	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this); // Инициализация мок-объектов
	}

	@Test
	public void testGetUsers() {

		List<UserDTO> expectedUsers = new ArrayList<>();
		UserDTO user1 = new UserDTO();
		UserDTO user2 = new UserDTO();
		user1.setEmail("john@example.com");
		user1.setFullName("John");
		user2.setEmail("jane@example.com");
		user2.setFullName("Jane");
		expectedUsers.add(user1);
		expectedUsers.add(user2);

		// Устанавливаем поведение мок-объектов userRepository и userMapper
		when(userRepository.findAll()).thenReturn(Collections.emptyList());
		when(userMapper.toDtoList(Collections.emptyList())).thenReturn(expectedUsers);

		// Вызываем метод, который нужно протестировать
		List<UserDTO> actualUsers = userService.getUsers();

		// Проверяем, совпадают ли ожидаемый и фактический результаты
		assertEquals(expectedUsers, actualUsers);
	}

	@Test
	public void testGetUser() {

		Long userId = 1L;
		User user = new User();
		user.setId(userId);
		user.setFullName("John");
		user.setEmail("john@example.com");

		// Задаем ожидаемый результат
		UserDTO expectedUser = new UserDTO();
		expectedUser.setId(userId);
		expectedUser.setFullName("John");
		expectedUser.setEmail("john@example.com");

		// Устанавливаем поведение мок-объектов userRepository и userMapper
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(userMapper.toDto(user)).thenReturn(expectedUser);

		// Вызываем метод, который нужно протестировать
		UserDTO actualUser = userService.getUser(userId);

		// Проверяем, совпадают ли ожидаемый и фактический результаты
		assertEquals(expectedUser, actualUser);
	}

	@Test
	public void testAddUser() {

		UserDTO newUser = new UserDTO();
		newUser.setEmail("john@example.com");
		newUser.setFullName("John");

		// Задаем ожидаемый результат
		UserDTO expectedUser = new UserDTO();
		expectedUser.setEmail("john@example.com");
		expectedUser.setFullName("John");

		// Устанавливаем поведение мок-объектов userRepository и userMapper
		User user = new User();
		user.setEmail(newUser.getEmail());
		user.setFullName(newUser.getFullName());
		when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);
		when(userMapper.toModel(newUser)).thenReturn(user);
		when(userMapper.toDto(user)).thenReturn(expectedUser);
		when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encodedPassword");
		when(userRepository.save(user)).thenReturn(user);

		// Вызываем метод, который нужно протестировать
		UserDTO actualUser = userService.addUser(newUser);

		// Проверяем, совпадают ли ожидаемый и фактический результаты
		assertEquals(expectedUser, actualUser);
	}

	@Test
	public void testRegisterUser() {

		UserDTO newUser = new UserDTO();
		newUser.setEmail("john@example.com");
		newUser.setFullName("John");

		// Задайте ожидаемый результат
		ResponseEntity<String> expectedResponse = ResponseEntity.ok("User registered successfully!");

		// Устанавливаем поведение мок-объектов userRepository и userMapper
		User user = new User();
		user.setEmail(newUser.getEmail());
		user.setFullName(newUser.getFullName());
		when(userRepository.findByEmail(newUser.getEmail())).thenReturn(null);
		when(userMapper.toModel(newUser)).thenReturn(user);
		when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encodedPassword");
		when(userRepository.save(user)).thenReturn(user);

		// Вызываем метод, который нужно протестировать
		ResponseEntity<String> actualResponse = userService.registerUser(newUser);

		// Проверяем, совпадают ли ожидаемый и фактический результаты
		assertEquals(expectedResponse, actualResponse);
	}

	@Test
	public void testUpdateUser() {

		UserDTO userToUpdate = new UserDTO();
		userToUpdate.setFullName("John Doe");
		userToUpdate.setEmail("johndoe@example.com");

		// Задаем ожидаемый результат
		UserDTO expectedUser = new UserDTO();
		expectedUser.setFullName("John Doe");
		expectedUser.setEmail("johndoe@example.com");

		// Устанавливаем поведение мок-объектов userRepository и userMapper
		User userToSave = userMapper.toModel(userToUpdate);
		User savedUser = userMapper.toModel(expectedUser);

		when(userMapper.toModel(userToUpdate)).thenReturn(userToSave);
		when(userMapper.toDto(savedUser)).thenReturn(expectedUser);
		when(userRepository.save(userToSave)).thenReturn(savedUser);

		// Вызываем метод updateUser
		UserDTO actualUser = userService.updateUser(userToUpdate);

		// Проверяем, совпадают ли ожидаемый и фактический результаты
		assertEquals(expectedUser, actualUser);
	}


	@Test
	public void testGetCourses() {

		List<CourseDTO> expectedCourses = new ArrayList<>();
		CourseDTO course1 = new CourseDTO();
		CourseDTO course2 = new CourseDTO();

		course1.setCourseName("Course 1");
		course1.setDescription("Description 1");
		course2.setCourseName("Course 2");
		course2.setDescription("Description 2");

		expectedCourses.add(course1);
		expectedCourses.add(course2);

		// Устанавливаем поведение мок-объекта courseService
		when(courseService.getCourses()).thenReturn(expectedCourses);

		// Вызываем метод, который нужно протестировать
		List<CourseDTO> actualCourses = courseService.getCourses();

		// Проверяем, совпадают ли ожидаемый и фактический результаты
		assertEquals(expectedCourses, actualCourses);
	}

	@Test
	public void testAddCourse() {

		// Создайте новый курс для добавления
		CourseDTO newCourse = new CourseDTO();
		newCourse.setCourseName("New Course");
		newCourse.setDescription("New Course Description");

		// Задаем ожидаемый результат
		CourseDTO expectedCourse = new CourseDTO();
		expectedCourse.setCourseName("New Course");
		expectedCourse.setDescription("New Course Description");

		// Устанавливаем поведение мок-объекта courseService
		when(courseService.addCourse(newCourse)).thenReturn(expectedCourse);

		// Вызываем метод, который нужно протестировать
		CourseDTO actualCourse = courseService.addCourse(newCourse);

		// Проверяем, совпадают ли ожидаемый и фактический результаты
		assertEquals(expectedCourse, actualCourse);
	}

	@Test
	public void testGetCourse() {

		Long courseId = 1L;

		CourseDTO expectedCourse = new CourseDTO();
		expectedCourse.setCourseName("Course 1");
		expectedCourse.setDescription("Description 1");

		// Устанавливаем поведение мок-объекта courseService
		when(courseService.getCourse(courseId)).thenReturn(expectedCourse);

		// Вызываем метод, который нужно протестировать
		CourseDTO actualCourse = courseService.getCourse(courseId);

		// Проверяем, совпадают ли ожидаемый и фактический результаты
		assertEquals(expectedCourse, actualCourse);
	}

	@Test
	public void testUpdateCourse() {

		CourseDTO courseToUpdate = new CourseDTO();
		courseToUpdate.setId(1L);
		courseToUpdate.setCourseName("Updated Course");
		courseToUpdate.setDescription("Updated Course Description");

		// Задаем ожидаемый результат
		CourseDTO expectedCourse = new CourseDTO();
		expectedCourse.setId(1L);
		expectedCourse.setCourseName("Updated Course");
		expectedCourse.setDescription("Updated Course Description");

		// Устанавливаем поведение мок-объекта courseService
		when(courseService.updateCourse(courseToUpdate)).thenReturn(expectedCourse);

		// Вызываем метод, который нужно протестировать
		CourseDTO actualCourse = courseService.updateCourse(courseToUpdate);

		// Проверяем, совпадают ли ожидаемый и фактический результаты
		assertEquals(expectedCourse, actualCourse);
	}


}
