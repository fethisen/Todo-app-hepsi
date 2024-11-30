package com.hepsi.emlak.todo_app.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "\"Please enter your first name.")
    @Size(min = 1, max = 100, message = "Your first name must be between 1 and 100 characters.")
    private String firstName;

    @NotBlank(message = "Please enter your last name.")
    @Size(min = 1, max = 50,message = "Your last name must be between 1 and 50 characters.")
    private String lastName;

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
