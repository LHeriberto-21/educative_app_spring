package com.educative.app.hlv.controllers;

import com.educative.app.hlv.dto.activity.ActivityRequest;
import com.educative.app.hlv.dto.activity.ActivityResponse;
import com.educative.app.hlv.models.User;
import com.educative.app.hlv.service.activity.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/subject/{subjectId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ActivityResponse>> findBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(activityService.findBySubject(subjectId));
    }

    @GetMapping("/subject/{subjectId}/semester/{semester}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ActivityResponse>> findBySubjectAndSemester(
            @PathVariable Long subjectId,
            @PathVariable Integer semester) {
        return ResponseEntity.ok(activityService.findBySubjectAndSemester(subjectId, semester));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ActivityResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ActivityResponse> create(
            @Valid @RequestBody ActivityRequest request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.create(request, user.getId()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<ActivityResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ActivityRequest request) {
        return ResponseEntity.ok(activityService.update(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        activityService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
