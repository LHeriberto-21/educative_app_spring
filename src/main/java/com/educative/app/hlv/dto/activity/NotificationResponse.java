package com.educative.app.hlv.dto.activity;

import com.educative.app.hlv.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private Boolean read;
    private LocalDateTime createdAt;
}
