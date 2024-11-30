package com.hepsi.emlak.todo_app;

import com.hepsi.emlak.todo_app.config.security.jwt.JwtUtils;
import com.hepsi.emlak.todo_app.domain.Todo;
import com.hepsi.emlak.todo_app.domain.User;
import com.hepsi.emlak.todo_app.service.TodoService;
import com.hepsi.emlak.todo_app.service.dto.request.TodoRequest;
import com.hepsi.emlak.todo_app.service.dto.response.TodoResponse;
import com.hepsi.emlak.todo_app.service.repository.TodoRepository;
import com.hepsi.emlak.todo_app.service.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void createTodo_ShouldCreateTodoSuccessfully() {
        // Arrange
        TodoRequest request = new TodoRequest();
        request.setTitle("Test Title");
        request.setDescription("Test Description");

        User mockUser = new User();
        mockUser.setId("userId123");
        mockUser.setEmail("test@example.com");

        Todo mockTodo = new Todo();
        mockTodo.setId("todoId123");
        mockTodo.setUserId(mockUser.getId());
        mockTodo.setTitle(request.getTitle());
        mockTodo.setDescription(request.getDescription());
        mockTodo.setCompleted(false);
        mockTodo.setCreatedAt(LocalDateTime.now());
        mockTodo.setUpdatedAt(LocalDateTime.now());

        Mockito.when(jwtUtils.getCurrentUserEmail()).thenReturn(mockUser.getEmail());
        Mockito.when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));
        Mockito.when(todoRepository.save(Mockito.any(Todo.class))).thenReturn(mockTodo);

        // Act
        TodoResponse response = todoService.createTodo(request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("todoId123", response.getTodoId());
        Assertions.assertEquals("Test Title", response.getTitle());
        Mockito.verify(todoRepository).save(Mockito.any(Todo.class));
    }

    @Test
    void getUserTodos_ShouldReturnTodosSuccessfully() {
        // Arrange
        User mockUser = new User();
        mockUser.setId("userId123");
        mockUser.setEmail("test@example.com");

        Todo mockTodo = new Todo();
        mockTodo.setId("todoId123");
        mockTodo.setUserId(mockUser.getId());
        mockTodo.setTitle("Test Title");
        mockTodo.setDescription("Test Description");

        Page<Todo> mockPage = new PageImpl<>(List.of(mockTodo));

        Mockito.when(jwtUtils.getCurrentUserEmail()).thenReturn(mockUser.getEmail());
        Mockito.when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));
        Mockito.when(todoRepository.findByUserId(Mockito.eq(mockUser.getId()), Mockito.any(Pageable.class))).thenReturn(mockPage);

        // Act
        Page<TodoResponse> response = todoService.getUserTodos(PageRequest.of(0, 10));

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getContent().size());
        Assertions.assertEquals("Test Title", response.getContent().get(0).getTitle());
    }

    @Test
    void updateTodo_ShouldUpdateTodoSuccessfully() {
        // Arrange
        TodoRequest request = new TodoRequest();
        request.setTitle("Updated Title");
        request.setDescription("Updated Description");
        request.setCompleted(true);

        User mockUser = new User();
        mockUser.setId("userId123");
        mockUser.setEmail("test@example.com");

        Todo mockTodo = new Todo();
        mockTodo.setId("todoId123");
        mockTodo.setUserId(mockUser.getId());
        mockTodo.setTitle("Old Title");
        mockTodo.setDescription("Old Description");

        Mockito.when(jwtUtils.getCurrentUserEmail()).thenReturn(mockUser.getEmail());
        Mockito.when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));
        Mockito.when(todoRepository.findById("todoId123")).thenReturn(Optional.of(mockTodo));
        Mockito.when(todoRepository.save(Mockito.any(Todo.class))).thenReturn(mockTodo);

        // Act
        TodoResponse response = todoService.updateTodo("todoId123", request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Updated Title", response.getTitle());
        Assertions.assertTrue(response.isCompleted());
    }

    @Test
    void deleteTodo_ShouldDeleteTodoSuccessfully() {
        // Arrange
        User mockUser = new User();
        mockUser.setId("userId123");
        mockUser.setEmail("test@example.com");

        Todo mockTodo = new Todo();
        mockTodo.setId("todoId123");
        mockTodo.setUserId(mockUser.getId());

        Mockito.when(jwtUtils.getCurrentUserEmail()).thenReturn(mockUser.getEmail());
        Mockito.when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(Optional.of(mockUser));
        Mockito.when(todoRepository.findById("todoId123")).thenReturn(Optional.of(mockTodo));

        // Act
        todoService.deleteTodo("todoId123");

        // Assert
        Mockito.verify(todoRepository).delete(mockTodo);
    }
}
