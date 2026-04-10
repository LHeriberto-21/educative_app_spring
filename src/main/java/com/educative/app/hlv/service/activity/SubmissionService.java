package com.educative.app.hlv.service.activity;

import com.educative.app.hlv.dto.activity.GradeSubmissionRequest;
import com.educative.app.hlv.dto.activity.SubmissionRequest;
import com.educative.app.hlv.dto.activity.SubmissionResponse;
import com.educative.app.hlv.enums.Role;
import com.educative.app.hlv.enums.SubmissionStatus;
import com.educative.app.hlv.exceptions.BusinessException;
import com.educative.app.hlv.exceptions.ResourceNotFoundException;
import com.educative.app.hlv.mapper.SubmissionMapper;
import com.educative.app.hlv.models.Activity;
import com.educative.app.hlv.models.ActivitySubmission;
import com.educative.app.hlv.models.User;
import com.educative.app.hlv.repo.ActivitySubmissionRepository;
import com.educative.app.hlv.service.UserService;
import com.educative.app.hlv.service.cloud.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final ActivitySubmissionRepository submissionRepository;
    private final ActivityService activityService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final SubmissionMapper submissionMapper;

    @Transactional(readOnly = true)
    public List<SubmissionResponse> findByActivity(Long activityId) {
        return submissionRepository.findByActivityId(activityId)
                .stream().map(submissionMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> findByStudent(Long studentId) {
        return submissionRepository.findByStudentId(studentId)
                .stream().map(submissionMapper::toResponse).collect(Collectors.toList());
    }


    @Transactional
    public SubmissionResponse submit(SubmissionRequest request) {
        User student = userService.findEntityById(request.getStudentId());
        Activity activity = activityService.findActivityOrThrow(request.getActivityId());

        if (!Role.STUDENT.equals(student.getRole())) {
            throw new BusinessException("Solo los alumnos pueden entregar actividades");
        }
        if (submissionRepository.existsByActivityIdAndStudentId(activity.getId(), student.getId())) {
            throw new BusinessException("El alumno ya realizó una entrega para esta actividad");
        }
        if (LocalDateTime.now().isAfter(activity.getDueDate())) {
            throw new BusinessException("La fecha límite de entrega ya pasó");
        }

        ActivitySubmission submission = submissionMapper.toEntity(activity, student);
        submission.setStatus(SubmissionStatus.SUBMITTED);
        submission.setSubmittedAt(LocalDateTime.now());

        return submissionMapper.toResponse(submissionRepository.save(submission));
    }

    @Transactional
    public SubmissionResponse gradeSubmission(Long submissionId, GradeSubmissionRequest request) {
        ActivitySubmission submission = findSubmissionOrThrow(submissionId);

        if (!SubmissionStatus.SUBMITTED.equals(submission.getStatus())) {
            throw new BusinessException("Solo se puede calificar una entrega que haya sido enviada");
        }

        submission.setScore(request.getScore());
        submission.setFeedback(request.getFeedback());
        submission.setStatus(SubmissionStatus.GRADED);

        ActivitySubmission saved = submissionRepository.save(submission);

        // Notificar al alumno que su entrega fue calificada
        notificationService.notifyGradeRegistered(
                submission.getStudent(),
                submission.getActivity().getSubject().getName(),
                request.getScore()
        );

        return submissionMapper.toResponse(saved);
    }

    private ActivitySubmission findSubmissionOrThrow(Long id) {
        return submissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrega no encontrada con id: " + id));
    }
}
