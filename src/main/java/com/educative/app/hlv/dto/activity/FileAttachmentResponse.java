package com.educative.app.hlv.dto.activity;

import com.educative.app.hlv.enums.FileRelatedType;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class FileAttachmentResponse {
    private Long id;
    private String fileName;
    private String fileUrl;       // URL pública de Cloudinary
    private Long fileSize;
    private String mimeType;
    private FileRelatedType relatedType;
    private Long relatedId;
    private LocalDateTime createdAt;
    private String uploadedByName;

}
