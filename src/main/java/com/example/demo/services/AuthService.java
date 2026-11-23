package com.example.demo.services;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entities.User;
import com.example.demo.enums.Role;
import com.example.demo.jwt.JwtService;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        User savedUser = userRepository.save(user);

        var userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(savedUser.getEmail())
                        .password(savedUser.getPassword())
                        .authorities("ROLE_" + savedUser.getRole().name())
                        .build();

        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                savedUser.getEmail(),
                savedUser.getRole().name()
        );
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail().trim().toLowerCase(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(
                request.getEmail().trim().toLowerCase()
        ).orElseThrow(() -> new RuntimeException("User not found"));

        var userDetails =
                org.springframework.security.core.userdetails.User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .authorities("ROLE_" + user.getRole().name())
                        .build();

        String token = jwtService.generateToken(userDetails);
        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
}
