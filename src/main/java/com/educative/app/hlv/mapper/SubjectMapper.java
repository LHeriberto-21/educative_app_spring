package com.educative.app.hlv.mapper;

import com.educative.app.hlv.dto.SubjectRequest;
import com.educative.app.hlv.dto.SubjectResponse;
import com.educative.app.hlv.models.Career;
import com.educative.app.hlv.models.Subject;
import com.educative.app.hlv.models.User;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {
    public Subject toEntity(SubjectRequest request, Career career, User teacher) {
        return Subject.builder()
                .name(request.getName().trim())
                .code(request.getCode().toUpperCase().trim())
                .description(request.getDescription())
                .credits(request.getCredits())
                .semester(request.getSemester())
                .maxStudents(request.getMaxStudents())
                .career(career)
                .teacher(teacher)
                .active(true)
                .build();
    }

    public SubjectResponse toResponse(Subject subject) {
        User teacher = subject.getTeacher();

        return SubjectResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .code(subject.getCode())
                .description(subject.getDescription())
                .credits(subject.getCredits())
                .semester(subject.getSemester())
                .maxStudents(subject.getMaxStudents())
                .active(subject.getActive())
                .careerId(subject.getCareer().getId())
                .careerName(subject.getCareer().getName())
                .teacherId(teacher != null ? teacher.getId() : null)
                .teacherFullName(teacher != null ? teacher.getFullName() : null)
                .teacherAcademicTitle(teacher != null ? teacher.getAcademicTitle() : null)
                .enrolledStudents(subject.getEnrollments().size())
                .availableSpots(subject.availableSpots())
                .build();
    }

    public void updateEntity(Subject subject, SubjectRequest request, Career career, User teacher) {
        subject.setName(request.getName().trim());
        subject.setCode(request.getCode().toUpperCase().trim());
        subject.setDescription(request.getDescription());
        subject.setCredits(request.getCredits());
        subject.setSemester(request.getSemester());
        subject.setMaxStudents(request.getMaxStudents());
        subject.setCareer(career);
        subject.setTeacher(teacher);
    }
}
