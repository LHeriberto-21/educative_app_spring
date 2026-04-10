package com.educative.app.hlv.service.activity;

import com.educative.app.hlv.dto.activity.ActivityRequest;
import com.educative.app.hlv.dto.activity.ActivityResponse;
import com.educative.app.hlv.enums.Role;
import com.educative.app.hlv.exceptions.BusinessException;
import com.educative.app.hlv.exceptions.ResourceNotFoundException;
import com.educative.app.hlv.mapper.ActivityMapper;
import com.educative.app.hlv.models.Activity;
import com.educative.app.hlv.models.Subject;
import com.educative.app.hlv.models.User;
import com.educative.app.hlv.repo.ActivityRepository;
import com.educative.app.hlv.repo.EnrollmentRepository;
import com.educative.app.hlv.service.SubjectService;
import com.educative.app.hlv.service.UserService;
import com.educative.app.hlv.service.cloud.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SubjectService subjectService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final ActivityMapper activityMapper;


    @Transactional(readOnly = true)
    public List<ActivityResponse> findBySubject(Long subjectId) {
        return activityRepository.findBySubjectIdAndActiveTrue(subjectId)
                .stream().map(activityMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> findBySubjectAndSemester(Long subjectId, Integer semester) {
        return activityRepository.findBySubjectIdAndSemesterAndActiveTrue(subjectId, semester)
                .stream().map(activityMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ActivityResponse findById(Long id) {
        return activityMapper.toResponse(findActivityOrThrow(id));
    }

    @Transactional
    public ActivityResponse create(ActivityRequest request, Long teacherId) {
        User teacher = userService.findEntityById(teacherId);

        if (!Role.TEACHER.equals(teacher.getRole()) && !Role.ADMIN.equals(teacher.getRole())) {
            throw new BusinessException("Solo los maestros pueden crear actividades");
        }

        Subject subject = subjectService.findSubjectOrThrow(request.getSubjectId());
        Activity activity = activityMapper.toEntity(request, subject, teacher);
        Activity saved = activityRepository.save(activity);

        // Notificar a todos los alumnos inscritos en la materia
        enrollmentRepository.findBySubjectId(subject.getId()).forEach(enrollment ->
                notificationService.notifyNewActivity(
                        enrollment.getStudent(),
                        saved.getTitle(),
                        subject.getName()
                )
        );

        return activityMapper.toResponse(saved);
    }

    @Transactional
    public ActivityResponse createByEmail(ActivityRequest request, String email) {
        User teacher = userService.findEntityByEmail(email);
        return create(request, teacher.getId());
    }

    @Transactional
    public ActivityResponse update(Long id, ActivityRequest request) {
        Activity activity = findActivityOrThrow(id);
        activityMapper.updateEntity(activity, request);
        return activityMapper.toResponse(activityRepository.save(activity));
    }

    @Transactional
    public void deactivate(Long id) {
        Activity activity = findActivityOrThrow(id);
        activity.setActive(false);
        activityRepository.save(activity);
    }

    public Activity findActivityOrThrow(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actividad no encontrada con id: " + id));
    }
}
