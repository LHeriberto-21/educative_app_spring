package com.educative.app.hlv.dto.enrollment;

import com.educative.app.hlv.enums.EnrollmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class EnrollmentResponse {
    // Inscripción
    private Long id;
    private EnrollmentStatus status;
    private LocalDate enrolledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Calificaciones
    private Double partialGrade1;
    private Double partialGrade2;
    private Double finalGrade;
    private Double average;
    private Boolean passing;
    private String observations;

    // datos del alumno
    private Long studentId;
    private String studentFullName;
    private String studentRegistrationNumber;

    // datos de la materia
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
}
