package com.adilet.dtos;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @Size(max = 500) String bio,
        @Size(max = 100) String city,
        @Size(max = 512) String avatarUrl
) {
}
