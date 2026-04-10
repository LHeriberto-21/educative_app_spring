package com.educative.app.hlv.service.profile;

import com.educative.app.hlv.dto.UserResponse;
import com.educative.app.hlv.dto.user.ChangePasswordRequest;
import com.educative.app.hlv.dto.user.UpdateProfileRequest;
import com.educative.app.hlv.exceptions.BusinessException;
import com.educative.app.hlv.exceptions.ResourceNotFoundException;
import com.educative.app.hlv.mapper.UserMapper;
import com.educative.app.hlv.models.User;
import com.educative.app.hlv.repo.UserRepository;
import com.educative.app.hlv.service.cloud.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public UserResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado"));
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado"));

        // Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new BusinessException("La contraseña actual es incorrecta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    @Transactional
    public UserResponse updateProfilePhoto(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado"));
        try {
            // Si ya tenía foto, eliminar la anterior de Cloudinary
            if (user.getProfilePhotoName() != null) {
                cloudinaryService.delete(user.getProfilePhotoName());
            }

            Map result = cloudinaryService.upload(file, "profiles");

            user.setProfilePhotoUrl((String) result.get("secure_url"));
            user.setProfilePhotoName((String) result.get("public_id"));

            return userMapper.toResponse(userRepository.save(user));

        } catch (Exception e) {
            throw new BusinessException("Error al actualizar la foto de perfil: " + e.getMessage());
        }
    }
}
