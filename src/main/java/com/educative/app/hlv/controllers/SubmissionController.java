package com.educative.app.hlv.controllers;

import com.educative.app.hlv.dto.activity.GradeSubmissionRequest;
import com.educative.app.hlv.dto.activity.SubmissionRequest;
import com.educative.app.hlv.dto.activity.SubmissionResponse;
import com.educative.app.hlv.service.activity.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/submission")
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @GetMapping("/activity/{activityId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<List<SubmissionResponse>> findByActivity(@PathVariable Long activityId) {
        return ResponseEntity.ok(submissionService.findByActivity(activityId));
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.username == @userService.findEntityById(#studentId).email")
    public ResponseEntity<List<SubmissionResponse>> findByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(submissionService.findByStudent(studentId));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<SubmissionResponse> submit(@Valid @RequestBody SubmissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(submissionService.submit(request));
    }

    @PatchMapping("/{id}/grade")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<SubmissionResponse> grade(
            @PathVariable Long id,
            @Valid @RequestBody GradeSubmissionRequest request) {
        return ResponseEntity.ok(submissionService.gradeSubmission(id, request));
    }
}
