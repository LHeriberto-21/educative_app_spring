package com.educative.app.hlv.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CareerResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer totalSemesters;
    private Boolean active;

    private Integer totalStudents;
    private Integer totalSubjects;
}
