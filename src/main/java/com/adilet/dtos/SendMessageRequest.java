package com.adilet.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SendMessageRequest(
        @NotBlank(message = "Message can't be empty")
        @Size(max = 1000)
        String text
) {
}
