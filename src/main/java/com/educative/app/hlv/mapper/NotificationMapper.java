package com.educative.app.hlv.mapper;

import com.educative.app.hlv.dto.activity.NotificationResponse;
import com.educative.app.hlv.enums.NotificationType;
import com.educative.app.hlv.models.Notification;
import com.educative.app.hlv.models.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public Notification toEntity(User recipient, String title, String message, NotificationType type) {
        return Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message)
                .type(type)
                .read(false)
                .build();
    }

    public NotificationResponse toResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .read(notification.getRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
