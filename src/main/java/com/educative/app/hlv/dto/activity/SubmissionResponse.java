package com.educative.app.hlv.dto.activity;

import com.educative.app.hlv.enums.SubmissionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class SubmissionResponse {
    private Long id;
    private SubmissionStatus status;
    private Double score;
    private String feedback;
    private LocalDateTime submittedAt;
    private LocalDateTime createdAt;

    private Long activityId;
    private String activityTitle;
    private Double activityMaxScore;

    private Long studentId;
    private String studentFullName;
    private String studentRegistrationNumber;
}
