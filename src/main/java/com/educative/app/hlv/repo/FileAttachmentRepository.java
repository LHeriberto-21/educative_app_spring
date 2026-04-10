package com.educative.app.hlv.repo;

import com.educative.app.hlv.enums.FileRelatedType;
import com.educative.app.hlv.models.FileAttachment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileAttachmentRepository extends AppRepository<FileAttachment,Long>{
    List<FileAttachment> findByRelatedTypeAndRelatedId(FileRelatedType relatedType, Long relatedId);

    // Archivos subidos por un usuario
    List<FileAttachment> findByUploadedById(Long userId);
}
