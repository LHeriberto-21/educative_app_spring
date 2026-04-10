package com.educative.app.hlv.service.cloud;

import com.educative.app.hlv.dto.activity.NotificationResponse;
import com.educative.app.hlv.enums.NotificationType;
import com.educative.app.hlv.mapper.NotificationMapper;
import com.educative.app.hlv.models.*;
import com.educative.app.hlv.repo.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    @Autowired
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Transactional(readOnly = true)
    public List<NotificationResponse> findAllByRecipient(Long recipientId) {
        return notificationRepository
                .findByRecipientIdOrderByCreatedAtDesc(recipientId)
                .stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> findUnreadByRecipient(Long recipientId) {
        return notificationRepository
                .findByRecipientIdAndReadFalseOrderByCreatedAtDesc(recipientId)
                .stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long countUnread(Long recipientId) {
        return notificationRepository.countByRecipientIdAndReadFalse(recipientId);
    }

    @Transactional
    public void markAllAsRead(Long recipientId) {
        notificationRepository.markAllAsRead(recipientId);
    }
    @Transactional
    public void notifyNewActivity(User recipient, String activityTitle, String subjectName) {
        send(recipient,
                "Nueva actividad: " + activityTitle,
                "Se publicó una nueva actividad en " + subjectName,
                NotificationType.NEW_ACTIVITY);
    }

    @Transactional
    public void notifyGradeRegistered(User recipient, String subjectName, Double grade) {
        send(recipient,
                "Calificación registrada",
                "Tu calificación en " + subjectName + " es: " + grade,
                NotificationType.GRADE_REGISTERED);
    }

    @Transactional
    public void notifyEnrollment(User recipient, String subjectName) {
        send(recipient,
                "Inscripción confirmada",
                "Quedaste inscrito en la materia: " + subjectName,
                NotificationType.ENROLLMENT);
    }

    @Transactional
    public void notifyDeadlineReminder(User recipient, String activityTitle, String dueDate) {
        send(recipient,
                "Recordatorio: " + activityTitle,
                "La actividad vence el " + dueDate + ". ¡No olvides entregar!",
                NotificationType.DEADLINE_REMINDER);
    }

    private void send(User recipient, String title, String message, NotificationType type) {
        Notification notification = notificationMapper.toEntity(recipient, title, message, type);
        notificationRepository.save(notification);
    }
}
