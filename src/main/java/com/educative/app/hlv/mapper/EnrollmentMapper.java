package com.educative.app.hlv.mapper;

import com.educative.app.hlv.dto.enrollment.EnrollmentResponse;
import com.educative.app.hlv.dto.enrollment.GradeRequest;
import com.educative.app.hlv.enums.EnrollmentStatus;
import com.educative.app.hlv.models.Enrollment;
import com.educative.app.hlv.models.Subject;
import com.educative.app.hlv.models.User;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {
    public Enrollment toEntity(User student, Subject subject) {
        return Enrollment.builder()
                .student(student)
                .subject(subject)
                .status(EnrollmentStatus.ACTIVE)
                .build();
    }

    public EnrollmentResponse toResponse(Enrollment enrollment) {
        User student = enrollment.getStudent();
        Subject subject = enrollment.getSubject();

        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .status(enrollment.getStatus())
                .enrolledAt(enrollment.getEnrolledAt())
                .createdAt(enrollment.getCreatedAt())
                .updatedAt(enrollment.getUpdatedAt())
                .partialGrade1(enrollment.getPartialGrade1())
                .partialGrade2(enrollment.getPartialGrade2())
                .finalGrade(enrollment.getFinalGrade())
                .average(enrollment.calculateAverage())
                .passing(enrollment.isPassing())
                .observations(enrollment.getObservations())
                .studentId(student.getId())
                .studentFullName(student.getFullName())
                .studentRegistrationNumber(student.getRegistrationNumber())
                .subjectId(subject.getId())
                .subjectName(subject.getName())
                .subjectCode(subject.getCode())
                .build();
    }
    public void applyGrades(Enrollment enrollment, GradeRequest request) {
        if (request.getPartialGrade1() != null) {
            enrollment.setPartialGrade1(request.getPartialGrade1());
        }
        if (request.getPartialGrade2() != null) {
            enrollment.setPartialGrade2(request.getPartialGrade2());
        }
        if (request.getFinalGrade() != null) {
            enrollment.setFinalGrade(request.getFinalGrade());
            enrollment.setStatus(
                    enrollment.isPassing() ? EnrollmentStatus.APPROVED : EnrollmentStatus.FAILED
            );
        }
        if (request.getObservations() != null) {
            enrollment.setObservations(request.getObservations());
        }
    }
}
