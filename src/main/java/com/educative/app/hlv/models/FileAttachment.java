package com.educative.app.hlv.models;

import com.educative.app.hlv.enums.FileRelatedType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_attachments")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"uploadedBy"})
public class FileAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quién subió el archivo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by_id", nullable = false)
    private User uploadedBy;

    // A qué tipo de entidad pertenece (ACTIVITY, SUBMISSION, PROFILE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FileRelatedType relatedType;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    // El id de la entidad relacionada
    @Column(nullable = false)
    private Long relatedId;

    @Column(nullable = false, length = 255)
    private String fileName;

    // URL pública de Cloudinary para acceder al archivo
    @Column(nullable = false, length = 500)
    private String fileUrl;

    // Id público de Cloudinary — necesario para eliminarlo después
    @Column(nullable = false, length = 255)
    private String cloudinaryPublicId;

    // Tamaño en bytes
    private Long fileSize;

    // Tipo MIME ("application/pdf", "image/jpeg", etc.)
    @Column(length = 100)
    private String mimeType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
