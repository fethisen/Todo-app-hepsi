package com.hepsi.emlak.todo_app.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hepsi.emlak.todo_app.service.dto.response.MessageDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        MessageDto messageDto = MessageDto.builder()
                .statusCode(HttpServletResponse.SC_UNAUTHORIZED)
                .message("Authentication failed. Please tyr logging in.")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse  = objectMapper.writeValueAsString(messageDto);
        response.getWriter().write(jsonResponse );
    }
}
