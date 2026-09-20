package com.adilet.controllers;

import com.adilet.dtos.UpdateProfileRequest;
import com.adilet.dtos.UserProfileResponse;
import com.adilet.security.UserPrincipal;
import com.adilet.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public UserProfileResponse me(@AuthenticationPrincipal UserPrincipal principal) {
        return userService.getProfile(principal.getId());
    }

    @GetMapping("/{id}")
    public UserProfileResponse profile(@PathVariable Long id) {
        return userService.getProfile(id);
    }

    @PutMapping("/me")
    public UserProfileResponse updateMe(@Valid @RequestBody UpdateProfileRequest request,
                                        @AuthenticationPrincipal UserPrincipal principal) {
        return userService.updateMyProfile(principal.getId(), request);
    }
}
