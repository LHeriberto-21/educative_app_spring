package com.educative.app.hlv.service;

import com.educative.app.hlv.dto.enrollment.EnrollmentRequest;
import com.educative.app.hlv.dto.enrollment.EnrollmentResponse;
import com.educative.app.hlv.dto.enrollment.GradeRequest;
import com.educative.app.hlv.enums.EnrollmentStatus;
import com.educative.app.hlv.enums.Role;
import com.educative.app.hlv.exceptions.BusinessException;
import com.educative.app.hlv.exceptions.ResourceNotFoundException;
import com.educative.app.hlv.mapper.EnrollmentMapper;
import com.educative.app.hlv.models.Subject;
import com.educative.app.hlv.models.User;
import com.educative.app.hlv.repo.EnrollmentRepository;
import com.educative.app.hlv.models.Enrollment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserService userService;
    private final SubjectService subjectService;
    private final EnrollmentMapper enrollmentMapper;

    @Transactional
    public List<EnrollmentResponse> findByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId)
                .stream()
                .map(enrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<EnrollmentResponse> findActiveByStudent(Long studentId) {
        return enrollmentRepository.findByStudentIdAndStatus(studentId, EnrollmentStatus.ACTIVE)
                .stream()
                .map(enrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<EnrollmentResponse> findBySubject(Long subjectId) {
        return enrollmentRepository.findBySubjectId(subjectId)
                .stream()
                .map(enrollmentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentResponse findById(Long id) {
        return enrollmentMapper.toResponse(findEnrollmentOrThrow(id));
    }

    @Transactional
    public EnrollmentResponse enroll(EnrollmentRequest request) {
        User student = userService.findEntityById(request.getStudentId());
        Subject subject = subjectService.findSubjectOrThrow(request.getSubjectId());

        // 1. Validar que el usuario sea alumno
        if (!Role.STUDENT.equals(student.getRole())) {
            throw new BusinessException("Solo los alumnos pueden inscribirse a materias");
        }

        // 2. Validar que la materia esté activa
        if (!subject.getActive()) {
            throw new BusinessException("La materia no está disponible para inscripción");
        }

        // 3. Validar que el alumno no esté ya inscrito
        if (enrollmentRepository.existsByStudentIdAndSubjectId(student.getId(), subject.getId())) {
            throw new BusinessException("El alumno ya está inscrito en esta materia");
        }

        // 4. Validar que haya cupo disponible
        long activeCount = enrollmentRepository.countActiveEnrollmentsBySubject(subject.getId());
        if (activeCount >= subject.getMaxStudents()) {
            throw new BusinessException("La materia no tiene cupo disponible");
        }
        // 5. Crear la inscripción
        Enrollment enrollment = enrollmentMapper.toEntity(student, subject);
        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Transactional
    public EnrollmentResponse registerGrades(Long enrollmentId, GradeRequest request) {
        Enrollment enrollment = findEnrollmentOrThrow(enrollmentId);

        // Solo se pueden registrar calificaciones si el alumno está activo
        if (!EnrollmentStatus.ACTIVE.equals(enrollment.getStatus())) {
            throw new BusinessException("No se pueden registrar calificaciones: el alumno no está activo en esta materia");
        }

        enrollmentMapper.applyGrades(enrollment, request);
        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }

    @Transactional
    public EnrollmentResponse withdraw(Long enrollmentId) {
        Enrollment enrollment = findEnrollmentOrThrow(enrollmentId);

        if (!EnrollmentStatus.ACTIVE.equals(enrollment.getStatus())) {
            throw new BusinessException("Solo se puede dar de baja una inscripción activa");
        }

        enrollment.setStatus(EnrollmentStatus.WITHDRAWN);
        return enrollmentMapper.toResponse(enrollmentRepository.save(enrollment));
    }


    private Enrollment findEnrollmentOrThrow(Long id) {
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada con id: " + id));
    }
}
