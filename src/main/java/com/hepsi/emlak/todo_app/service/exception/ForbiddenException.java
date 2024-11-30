package com.hepsi.emlak.todo_app.service.exception;

public class ForbiddenException  extends RuntimeException{
    public ForbiddenException(String message) {
        super(message);
    }
}

