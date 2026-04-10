package com.educative.app.hlv.mapper;

import com.educative.app.hlv.dto.activity.ActivityRequest;
import com.educative.app.hlv.dto.activity.ActivityResponse;
import com.educative.app.hlv.models.*;
import org.springframework.stereotype.Component;

@Component
public class ActivityMapper {

    public Activity toEntity(ActivityRequest request, Subject subject, User createdBy) {
        return Activity.builder()
                .title(request.getTitle().trim())
                .description(request.getDescription())
                .dueDate(request.getDueDate())
                .maxScore(request.getMaxScore())
                .type(request.getType())
                .semester(request.getSemester())
                .subject(subject)
                .createdBy(createdBy)
                .active(true)
                .build();
    }

    public ActivityResponse toResponse(Activity activity) {
        return ActivityResponse.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .description(activity.getDescription())
                .dueDate(activity.getDueDate())
                .maxScore(activity.getMaxScore())
                .type(activity.getType())
                .semester(activity.getSemester())
                .active(activity.getActive())
                .createdAt(activity.getCreatedAt())
                .subjectId(activity.getSubject().getId())
                .subjectName(activity.getSubject().getName())
                .teacherId(activity.getCreatedBy().getId())
                .teacherFullName(activity.getCreatedBy().getFullName())
                .submissionsCount(activity.getSubmissions().size())
                .build();
    }

    public void updateEntity(Activity activity, ActivityRequest request) {
        activity.setTitle(request.getTitle().trim());
        activity.setDescription(request.getDescription());
        activity.setDueDate(request.getDueDate());
        activity.setMaxScore(request.getMaxScore());
        activity.setType(request.getType());
        activity.setSemester(request.getSemester());
    }
}
