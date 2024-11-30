package com.hepsi.emlak.todo_app.service.exception;

public class JwtTokenExpiredException extends RuntimeException{
    private String email;
    public JwtTokenExpiredException(String message, String email) {
        super(message);
        this.email = email;
    }
    public String getEmail() {return email;}
}
