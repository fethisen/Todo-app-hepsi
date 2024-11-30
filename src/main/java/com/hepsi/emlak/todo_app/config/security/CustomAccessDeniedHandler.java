package com.hepsi.emlak.todo_app.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hepsi.emlak.todo_app.service.dto.response.MessageDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        MessageDto messageDto = MessageDto.builder()
                .statusCode(HttpServletResponse.SC_FORBIDDEN)
                .message("You don't have permission to access this content. For more information, please contact your system administrator.")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse  = objectMapper.writeValueAsString(messageDto);
        response.getWriter().write(jsonResponse );
    }
}
