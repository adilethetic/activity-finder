package com.adilet.services;

import com.adilet.dtos.UpdateProfileRequest;
import com.adilet.dtos.UserProfileResponse;
import com.adilet.entities.User;
import com.adilet.exception.NotFoundException;
import com.adilet.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private User getUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(Long userId){
        return UserProfileResponse.from(getUser(userId));
    }

    @Transactional
    public UserProfileResponse updateMyProfile(Long currentUserId, UpdateProfileRequest request) {
        User user = getUser(currentUserId);

        user.setBio(request.bio());
        user.setCity(request.city());
        user.setAvatarUrl(request.avatarUrl());

        return UserProfileResponse.from(user);
    }
}
