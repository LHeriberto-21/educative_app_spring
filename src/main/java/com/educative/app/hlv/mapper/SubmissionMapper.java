package com.educative.app.hlv.mapper;

import com.educative.app.hlv.dto.activity.SubmissionResponse;
import com.educative.app.hlv.enums.SubmissionStatus;
import com.educative.app.hlv.models.Activity;
import com.educative.app.hlv.models.ActivitySubmission;
import com.educative.app.hlv.models.User;
import org.springframework.stereotype.Component;

@Component
public class SubmissionMapper {

    public ActivitySubmission toEntity(Activity activity, User student) {
        return ActivitySubmission.builder()
                .activity(activity)
                .student(student)
                .status(SubmissionStatus.PENDING)
                .build();
    }

    public SubmissionResponse toResponse(ActivitySubmission submission) {
        return SubmissionResponse.builder()
                .id(submission.getId())
                .status(submission.getStatus())
                .score(submission.getScore())
                .feedback(submission.getFeedback())
                .submittedAt(submission.getSubmittedAt())
                .createdAt(submission.getCreatedAt())
                .activityId(submission.getActivity().getId())
                .activityTitle(submission.getActivity().getTitle())
                .activityMaxScore(submission.getActivity().getMaxScore())
                .studentId(submission.getStudent().getId())
                .studentFullName(submission.getStudent().getFullName())
                .studentRegistrationNumber(submission.getStudent().getRegistrationNumber())
                .build();
    }
}
