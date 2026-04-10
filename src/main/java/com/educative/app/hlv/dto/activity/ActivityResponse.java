package com.educative.app.hlv.dto.activity;

import com.educative.app.hlv.enums.ActivityType;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class ActivityResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private Double maxScore;
    private ActivityType type;
    private Integer semester;
    private Boolean active;
    private LocalDateTime createdAt;

    private Long subjectId;
    private String subjectName;

    private Long teacherId;
    private String teacherFullName;

    private Integer submissionsCount;
}
