package com.educative.app.hlv.service;

import com.educative.app.hlv.dto.UserRequestDto;
import com.educative.app.hlv.dto.UserResponse;
import com.educative.app.hlv.enums.Role;
import com.educative.app.hlv.exceptions.BusinessException;
import com.educative.app.hlv.exceptions.ResourceNotFoundException;
import com.educative.app.hlv.mapper.UserMapper;
import com.educative.app.hlv.models.Career;
import com.educative.app.hlv.models.User;
import com.educative.app.hlv.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CareerServiceImp careerService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findByRole(Role role) {

        List<User> users = userRepository.findByRoleWithCareer(role);
        return users.stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return userMapper.toResponse(findUserOrThrow(id));
    }

    @Transactional
    public UserResponse create(UserRequestDto request) {
        // 1. Validar email único
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El email ya está registrado: " + request.getEmail());
        }

        // 2. Si es alumno, validar matrícula única y que tenga carrera
        if (Role.STUDENT.equals(request.getRole())) {
            validateStudentFields(request);
        }

        // 3. Cargar la carrera si aplica
        Career career = loadCareerIfNeeded(request);

        // 4. Crear la entidad y encriptar password
        User user = userMapper.toEntity(request, career);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Transactional
    public UserResponse update(Long id, UserRequestDto request) {
        User user = findUserOrThrow(id);

        // Validar email solo si cambió
        if (!user.getEmail().equalsIgnoreCase(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("El email ya está en uso: " + request.getEmail());
        }

        Career career = loadCareerIfNeeded(request);
        userMapper.updateEntity(user, request, career);

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
//            changePassword(id, request.getPassword());
        }

        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void changePassword(Long id, String newPassword) {
        User user = findUserOrThrow(id);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void deactivate(Long id) {
        User user = findUserOrThrow(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void activate(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findEntityById(Long id) {
        return findUserOrThrow(id);
    }

    private void validateStudentFields(UserRequestDto request) {
        if (request.getRegistrationNumber() == null || request.getRegistrationNumber().isBlank()) {
            throw new BusinessException("El número de matrícula es obligatorio para alumnos");
        }
        if (userRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new BusinessException("La matrícula ya está registrada: " + request.getRegistrationNumber());
        }
        if (request.getCareerId() == null) {
            throw new BusinessException("La carrera es obligatoria para alumnos");
        }
    }

    private Career loadCareerIfNeeded(UserRequestDto request) {
        if (Role.STUDENT.equals(request.getRole()) && request.getCareerId() != null) {
            return careerService.findEntityById(request.getCareerId());
        }
        return null;
    }


    @Transactional(readOnly = true)
    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + email));
    }

    private @NonNull User findUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }
}
