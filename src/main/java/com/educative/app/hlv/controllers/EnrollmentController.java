package com.educative.app.hlv.controllers;

import com.educative.app.hlv.dto.enrollment.EnrollmentRequest;
import com.educative.app.hlv.dto.enrollment.EnrollmentResponse;
import com.educative.app.hlv.dto.enrollment.GradeRequest;
import com.educative.app.hlv.service.EnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #studentId")
    public ResponseEntity<List<EnrollmentResponse>> findByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.findByStudent(studentId));
    }

    @GetMapping("/student/{studentId}/active")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #studentId")
    public ResponseEntity<List<EnrollmentResponse>> findActiveByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.findActiveByStudent(studentId));
    }

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<EnrollmentResponse>> findBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(enrollmentService.findBySubject(subjectId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<EnrollmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<EnrollmentResponse> enroll(@Valid @RequestBody EnrollmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enroll(request));
    }

    @PatchMapping("/{id}/grades")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<EnrollmentResponse> registerGrades(
            @PathVariable Long id,
            @Valid @RequestBody GradeRequest request) {
        return ResponseEntity.ok(enrollmentService.registerGrades(id, request));
    }

    @PatchMapping("/{id}/withdraw")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<EnrollmentResponse> withdraw(@PathVariable Long id) {
        return ResponseEntity.ok(enrollmentService.withdraw(id));
    }
}
