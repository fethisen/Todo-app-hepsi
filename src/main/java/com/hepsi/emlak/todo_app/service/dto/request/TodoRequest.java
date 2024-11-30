package com.hepsi.emlak.todo_app.service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoRequest {
    @NotBlank(message = "Please enter the title information")
    private String title;
    private String description;
    private boolean completed;
}
