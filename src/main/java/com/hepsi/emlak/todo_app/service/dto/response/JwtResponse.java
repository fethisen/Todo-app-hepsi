package com.hepsi.emlak.todo_app.service.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponse {
    private String jwt;
    private String id;
    private String email;
    private List<String> roles;

    public JwtResponse(String jwt, String id, String email, List<String> roles) {
        this.jwt = jwt;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}
