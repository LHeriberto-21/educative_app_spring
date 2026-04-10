package com.educative.app.hlv.controllers;

import com.educative.app.hlv.dto.CareerRequest;
import com.educative.app.hlv.dto.CareerResponse;
import com.educative.app.hlv.service.CareerServiceImp;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/careers")
@RequiredArgsConstructor
public class CareerController {

    private final CareerServiceImp careerServiceImp;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CareerResponse>> findAllActive() {
        return ResponseEntity.ok(careerServiceImp.findAll());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CareerResponse>> findAll() {
        return ResponseEntity.ok(careerServiceImp.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CareerResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(careerServiceImp.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CareerResponse> create(@Valid @RequestBody CareerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(careerServiceImp.create(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CareerResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CareerRequest request) {
        return ResponseEntity.ok(careerServiceImp.update(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        careerServiceImp.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
