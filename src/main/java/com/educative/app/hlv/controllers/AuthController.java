package com.educative.app.hlv.controllers;

import com.educative.app.hlv.dto.UserRequestDto;
import com.educative.app.hlv.dto.UserResponse;
import com.educative.app.hlv.dto.auth.LoginRequest;
import com.educative.app.hlv.dto.auth.LoginResponse;
import com.educative.app.hlv.enums.Role;
import com.educative.app.hlv.service.AuthService;
import com.educative.app.hlv.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    // para logearse con las credenciales que les demos
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRequestDto request) {
        UserRequestDto userRequest = UserRequestDto.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .registrationNumber(request.getRegistrationNumber())
                .currentSemester(request.getCurrentSemester())
                .careerId(request.getCareerId())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userRequest));
    }

    @PostMapping("/register-admin")
    public ResponseEntity<UserResponse> registracionAdmin(
            @Valid @RequestBody UserRequestDto request) {
        UserRequestDto adminRequest = UserRequestDto.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.ADMIN)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(adminRequest));
    }
}
