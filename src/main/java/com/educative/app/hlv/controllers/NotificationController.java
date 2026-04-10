package com.educative.app.hlv.controllers;

import com.educative.app.hlv.dto.activity.NotificationResponse;
import com.educative.app.hlv.service.cloud.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{recipientId}")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #recipientId")
    public ResponseEntity<List<NotificationResponse>> findAll(@PathVariable Long recipientId) {
        return ResponseEntity.ok(notificationService.findAllByRecipient(recipientId));
    }

    @GetMapping("/{recipientId}/unread")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #recipientId")
    public ResponseEntity<List<NotificationResponse>> findUnread(@PathVariable Long recipientId) {
        return ResponseEntity.ok(notificationService.findUnreadByRecipient(recipientId));
    }

    @GetMapping("/{recipientId}/count")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #recipientId")
    public ResponseEntity<Long> countUnread(@PathVariable Long recipientId) {
        return ResponseEntity.ok(notificationService.countUnread(recipientId));
    }

    @PatchMapping("/{recipientId}/read-all")
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #recipientId")
    public ResponseEntity<Void> markAllAsRead(@PathVariable Long recipientId) {
        notificationService.markAllAsRead(recipientId);
        return ResponseEntity.noContent().build();
    }
}
