package com.educative.app.hlv.mapper;

import com.educative.app.hlv.dto.activity.FileAttachmentResponse;
import com.educative.app.hlv.enums.FileRelatedType;
import com.educative.app.hlv.models.FileAttachment;
import com.educative.app.hlv.models.User;
import org.springframework.stereotype.Component;

@Component
public class FileAttachmentMapper {

    public FileAttachment toEntity(User uploadedBy, FileRelatedType relatedType,
                                   Long relatedId, String fileName,
                                   String fileUrl, String cloudinaryPublicId,
                                   Long fileSize, String mimeType) {
        return FileAttachment.builder()
                .uploadedBy(uploadedBy)
                .relatedType(relatedType)
                .relatedId(relatedId)
                .fileName(fileName)
                .fileUrl(fileUrl)
                .cloudinaryPublicId(cloudinaryPublicId)
                .fileSize(fileSize)
                .mimeType(mimeType)
                .build();
    }

    public FileAttachmentResponse toResponse(FileAttachment file) {
        return FileAttachmentResponse.builder()
                .id(file.getId())
                .fileName(file.getFileName())
                .fileUrl(file.getFileUrl())
                .fileSize(file.getFileSize())
                .mimeType(file.getMimeType())
                .relatedType(file.getRelatedType())
                .relatedId(file.getRelatedId())
                .createdAt(file.getCreatedAt())
                .uploadedByName(file.getUploadedBy().getFullName())
                .build();
    }
}
