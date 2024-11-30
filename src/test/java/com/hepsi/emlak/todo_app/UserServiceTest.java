package com.hepsi.emlak.todo_app;

import com.hepsi.emlak.todo_app.domain.User;
import com.hepsi.emlak.todo_app.service.UserService;
import com.hepsi.emlak.todo_app.service.dto.request.RegisterRequest;
import com.hepsi.emlak.todo_app.service.exception.EmailAlreadyUsedException;
import com.hepsi.emlak.todo_app.service.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_ShouldSaveUser_WhenEmailIsNotUsed(){
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("123");
        request.setFirstName("Test");
        request.setLastName("User");

        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        Mockito.when(encoder.encode(request.getPassword())).thenReturn("encodedPassword");

        userService.register(request);

        Mockito.verify(userRepository).save(Mockito.argThat(user ->
                user.getEmail().equals(request.getEmail()) &&
                        user.getPassword().equals("encodedPassword") &&
                        user.getFirstName().equals(request.getFirstName()) &&
                        user.getLastName().equals(request.getLastName()) &&
                        user.getRoles().contains("ROLE_USER")
        ));
    }

    @Test
    void register_ShouldThrowException_WhenEmailIsAlreadyUsed() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");

        User existingUser = new User();
        existingUser.setEmail(request.getEmail());

        Mockito.when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        // Act & Assert
        Assertions.assertThrows(EmailAlreadyUsedException.class, () -> userService.register(request));
    }

}
