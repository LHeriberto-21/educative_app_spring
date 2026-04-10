package com.educative.app.hlv.mapper;

import com.educative.app.hlv.dto.CareerRequest;
import com.educative.app.hlv.dto.CareerResponse;
import com.educative.app.hlv.models.Career;
import org.springframework.stereotype.Component;

@Component
public class CareerMapper {
    public Career toEntity(CareerRequest request) {
        return Career.builder()
                .name(request.getName().trim())
                .code(request.getCode().toUpperCase().trim())
                .description(request.getDescription())
                .totalSemesters(request.getTotalSemesters())
                .active(true)
                .build();
    }

    public CareerResponse toResponse(Career career) {
        return CareerResponse.builder()
                .id(career.getId())
                .name(career.getName())
                .code(career.getCode())
                .description(career.getDescription())
                .totalSemesters(career.getTotalSemesters())
                .active(career.getActive())
                .totalStudents(career.getStudents() != null ? career.getStudents().size() : 0 )
                .totalSubjects(career.getSubjects() != null ? career.getSubjects().size() : 0)
                .build();
    }

    public void updateEntity(Career career, CareerRequest request) {
        career.setName(request.getName().trim());
        career.setCode(request.getCode().toUpperCase().trim());
        career.setDescription(request.getDescription());
        career.setTotalSemesters(request.getTotalSemesters());
    }
}
