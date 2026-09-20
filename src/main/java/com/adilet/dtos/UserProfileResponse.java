package com.adilet.dtos;

import com.adilet.entities.User;
import java.time.Instant;

public record UserProfileResponse(
        Long id,
        String username,
        String city,
        String bio,
        String avatarUrl,
        Instant createdAt
) {
    public static UserProfileResponse from(User user) {
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getCity(),
                user.getBio(),
                user.getAvatarUrl(),
                user.getCreatedAt()
        );
    }
}
