package com.educative.app.hlv.service.file;

import com.educative.app.hlv.dto.activity.FileAttachmentResponse;
import com.educative.app.hlv.enums.FileRelatedType;
import com.educative.app.hlv.exceptions.*;
import com.educative.app.hlv.mapper.FileAttachmentMapper;
import com.educative.app.hlv.models.*;
import com.educative.app.hlv.repo.FileAttachmentRepository;
import com.educative.app.hlv.service.UserService;
import com.educative.app.hlv.service.cloud.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileAttachmentRepository fileAttachmentRepository;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;
    private final FileAttachmentMapper fileAttachmentMapper;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Transactional(readOnly = true)
    public List<FileAttachmentResponse> findByRelated(FileRelatedType type, Long relatedId) {
        return fileAttachmentRepository.findByRelatedTypeAndRelatedId(type, relatedId)
                .stream().map(fileAttachmentMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public FileAttachmentResponse upload(MultipartFile file, FileRelatedType relatedType,
                                         Long relatedId, Long uploadedById) {
        if (file.isEmpty()) {
            throw new BusinessException("El archivo está vacío");
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException("El archivo supera el límite de 10 MB");
        }

        User uploader = userService.findEntityById(uploadedById);

        // Carpeta en Cloudinary según el tipo
        String folder = switch (relatedType) {
            case PROFILE    -> "profiles";
            case ACTIVITY   -> "activities";
            case SUBMISSION -> "submissions";
        };

        try {
            Map result = cloudinaryService.upload(file, folder);

            FileAttachment attachment = fileAttachmentMapper.toEntity(
                    uploader,
                    relatedType,
                    relatedId,
                    file.getOriginalFilename(),
                    (String) result.get("secure_url"),
                    (String) result.get("public_id"),
                    file.getSize(),
                    file.getContentType()
            );

            return fileAttachmentMapper.toResponse(fileAttachmentRepository.save(attachment));

        } catch (IOException e) {
            throw new BusinessException("Error al subir el archivo: " + e.getMessage());
        }
    }

    @Transactional
    public void delete(Long fileId) {
        FileAttachment file = fileAttachmentRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("Archivo no encontrado con id: " + fileId));
        try {
            cloudinaryService.delete(file.getCloudinaryPublicId());
            fileAttachmentRepository.delete(file);
        } catch (IOException e) {
            throw new BusinessException("Error al eliminar el archivo: " + e.getMessage());
        }
    }

}
