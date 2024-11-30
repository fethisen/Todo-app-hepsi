package com.hepsi.emlak.todo_app.service.exception;

public class EmailAlreadyUsedException extends BusinessException {
    public EmailAlreadyUsedException() {
        super("Sorry, this email address is already registered.");
    }
}
