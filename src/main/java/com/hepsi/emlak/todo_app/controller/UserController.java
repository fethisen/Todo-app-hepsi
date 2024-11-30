package com.hepsi.emlak.todo_app.controller;

import com.hepsi.emlak.todo_app.config.security.jwt.JwtUtils;
import com.hepsi.emlak.todo_app.service.UserDetailsImpl;
import com.hepsi.emlak.todo_app.service.UserService;
import com.hepsi.emlak.todo_app.service.dto.response.JwtResponse;
import com.hepsi.emlak.todo_app.service.dto.request.LoginRequest;
import com.hepsi.emlak.todo_app.service.dto.response.MessageDto;
import com.hepsi.emlak.todo_app.service.dto.request.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<MessageDto> register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        MessageDto messageDto = MessageDto.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Your user account has been successfully created.")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(messageDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles));
    }
}