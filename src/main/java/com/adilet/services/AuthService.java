package com.adilet.services;

import com.adilet.dtos.AuthResponse;
import com.adilet.dtos.LoginRequest;
import com.adilet.dtos.RegisterRequest;
import com.adilet.entities.User;
import com.adilet.exception.ConflictException;
import com.adilet.repositories.UserRepository;
import com.adilet.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.adilet.security.UserPrincipal;
import com.adilet.enums.Role;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("This email already exists");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("This username already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setCity(request.city());
        user.setRole(Role.USER);

        User saved = userRepository.save(user);

        return new AuthResponse(
                jwtService.generateToken(new UserPrincipal(saved)),
                saved.getId(),
                saved.getUsername(),
                saved.getEmail()
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return new AuthResponse(
                jwtService.generateToken(principal),
                principal.getId(),
                principal.getDisplayName(),
                principal.getEmail()
        );
    }


}
