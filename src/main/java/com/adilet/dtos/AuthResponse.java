package com.adilet.dtos;

public record AuthResponse(
        String token,
        Long userId,
        String username,
        String email
) {
}
