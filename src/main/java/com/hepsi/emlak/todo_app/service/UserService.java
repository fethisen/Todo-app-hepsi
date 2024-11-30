package com.hepsi.emlak.todo_app.service;

import com.hepsi.emlak.todo_app.domain.User;
import com.hepsi.emlak.todo_app.service.dto.request.RegisterRequest;
import com.hepsi.emlak.todo_app.service.exception.EmailAlreadyUsedException;
import com.hepsi.emlak.todo_app.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public void register(RegisterRequest request) {
        Optional<User> optUser = userRepository.findByEmail(request.getEmail());
        if (!optUser.isEmpty()){
            throw new EmailAlreadyUsedException();
        }

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_USER");

        User user = new User();
        user.setId(java.util.UUID.randomUUID().toString());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRoles(roles);
        userRepository.save(user);
    }

}
