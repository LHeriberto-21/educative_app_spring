package com.educative.app.hlv.controllers;

import com.educative.app.hlv.dto.SubjectRequest;
import com.educative.app.hlv.dto.SubjectResponse;
import com.educative.app.hlv.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SubjectResponse>> findAll() {
        return ResponseEntity.ok(subjectService.findAll());
    }

    @GetMapping("/filter")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SubjectResponse>> findByFilter(
            @RequestParam(required = false) Long careerId,
            @RequestParam(required = false) Integer semester) {

        if (careerId != null && semester != null) {
            return ResponseEntity.ok(subjectService.findByCareerAndSemester(careerId, semester));
        }
        if (careerId != null) {
            return ResponseEntity.ok(subjectService.findByCareer(careerId));
        }
        return ResponseEntity.ok(subjectService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SubjectResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> create(@Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody SubjectRequest request) {
        return ResponseEntity.ok(subjectService.update(id, request));
    }

    @PatchMapping("/{id}/teacher/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> assignTeacher(
            @PathVariable Long id,
            @PathVariable Long teacherId) {
        return ResponseEntity.ok(subjectService.assignTeacher(id, teacherId));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        subjectService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
