package com.educative.app.hlv.dto;

import com.educative.app.hlv.enums.Role;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String lastname;
    private String fullName;
    private String email;
    private Role role;
    private Boolean active;
    private LocalDateTime createdAt;


    // campos de alumno
    private String registrationNumber;
    private Integer currentSemester;
    private Long careerId;
    private String careerName;
    private String profilePhotoUrl;

    // campos de maestro
    private String academicTitle;
}
