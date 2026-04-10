package com.educative.app.hlv.mapper;

import com.educative.app.hlv.dto.UserRequestDto;
import com.educative.app.hlv.dto.UserResponse;
import com.educative.app.hlv.models.Career;
import com.educative.app.hlv.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
     public User toEntity(UserRequestDto userRequestDto, Career career){
         return User.builder()
                 .name(userRequestDto.getName())
                 .lastname(userRequestDto.getLastname())
                 .email(userRequestDto.getEmail().toLowerCase().trim())
                 .password(userRequestDto.getPassword())
                 .role(userRequestDto.getRole())
                 .registrationNumber(userRequestDto.getRegistrationNumber())
                 .currentSemester(userRequestDto.getCurrentSemester())
                 .academicTitle(userRequestDto.getAcademicTitle())
                 .career(career)
                 .active(true)
                 .build();
     }
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .lastname(user.getLastname())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .registrationNumber(user.getRegistrationNumber())
                .currentSemester(user.getCurrentSemester())
                .academicTitle(user.getAcademicTitle())
                .careerId(user.getCareer() != null ? user.getCareer().getId() : null)
                .profilePhotoUrl(user.getProfilePhotoUrl())
                .careerName(user.getCareer() != null ? user.getCareer().getName() : null)
                .build();
    }

    public void updateEntity(User user, UserRequestDto request, Career career) {
        user.setName(request.getName());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail().toLowerCase().trim());
        user.setRole(request.getRole());
        user.setRegistrationNumber(request.getRegistrationNumber());
        user.setCurrentSemester(request.getCurrentSemester());
        user.setAcademicTitle(request.getAcademicTitle());
        user.setCareer(career);
    }
}
