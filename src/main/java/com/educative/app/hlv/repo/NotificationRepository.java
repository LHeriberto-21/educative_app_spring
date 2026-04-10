package com.educative.app.hlv.repo;

import com.educative.app.hlv.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);

    // solo las no leídas
    List<Notification> findByRecipientIdAndReadFalseOrderByCreatedAtDesc(Long recipientId);

    // Cuántas no leídas tiene el usuario (para el badge en la app)
    long countByRecipientIdAndReadFalse(Long recipientId);

    // Marcar todas las notificaciones de un usuario como leídas de un solo golpe
    // @Modifying indica que esta query modifica datos (UPDATE/DELETE), no solo lee
    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.recipient.id = :recipientId AND n.read = false")
    void markAllAsRead(@Param("recipientId") Long recipientId);

}
