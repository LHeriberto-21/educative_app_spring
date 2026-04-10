package com.educative.app.hlv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer credits;
    private Integer semester;
    private Integer maxStudents;
    private Boolean active;

    private Long careerId;
    private String careerName;

    private Long teacherId;
    private String teacherFullName;
    private String teacherAcademicTitle;

    private Integer enrolledStudents;

    private Integer availableSpots;
}
