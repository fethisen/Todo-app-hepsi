package com.hepsi.emlak.todo_app.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Please enter your email address.")
    @Size(max = 50,message = "Your email address must be a maximum of 50 characters.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Please enter your password.")
    @Size(min = 8, max = 20,message = "Your password must be between 8 and 20 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Your password must contain at least one uppercase letter, one lowercase letter, and one number..")
    private String password;
}
