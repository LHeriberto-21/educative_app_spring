package com.educative.app.hlv.controllers;

import com.educative.app.hlv.dto.activity.FileAttachmentResponse;
import com.educative.app.hlv.enums.FileRelatedType;
import com.educative.app.hlv.mapper.FileAttachmentMapper;
import com.educative.app.hlv.models.User;
import com.educative.app.hlv.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<FileAttachmentResponse>> findFiles(
            @RequestParam FileRelatedType type,
            @RequestParam Long relatedId
            ) {
        return ResponseEntity.ok(fileService.findByRelated(type, relatedId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FileAttachmentResponse> upload(
            @RequestParam MultipartFile file,
            @RequestParam FileRelatedType relatedType,
            @RequestParam Long relatedId,
            @AuthenticationPrincipal User user ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fileService.upload(file, relatedType, relatedId, user.getId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fileService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
