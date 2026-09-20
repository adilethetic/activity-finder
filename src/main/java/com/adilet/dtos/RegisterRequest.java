package com.adilet.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50)
        String username,


        @NotBlank(message = "Email is required")
        @Email(message = "Incorrect email")
        @Size(max = 255)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password from 8 to 72 characters")
        String password,

        @Size(max = 100, message = "City is required")
        String city

) {
}
