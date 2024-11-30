package com.hepsi.emlak.todo_app.service.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MessageDto {
    int statusCode;
    String message;

    public MessageDto() {
    }

    public MessageDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}