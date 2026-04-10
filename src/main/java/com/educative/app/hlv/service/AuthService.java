package com.educative.app.hlv.service;

import com.educative.app.hlv.dto.auth.LoginRequest;
import com.educative.app.hlv.dto.auth.LoginResponse;
import com.educative.app.hlv.exceptions.BusinessException;
import com.educative.app.hlv.models.User;
import com.educative.app.hlv.repo.UserRepository;
import com.educative.app.hlv.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);


    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Credenciales incorrectas"));

        if (!user.getActive()) {
            throw new BusinessException("La cuenta está desactivada");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("Credenciales incorrectas");
        }

        String token = jwtService.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
