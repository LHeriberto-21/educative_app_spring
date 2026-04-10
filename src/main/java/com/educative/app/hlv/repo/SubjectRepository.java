package com.educative.app.hlv.repo;

import com.educative.app.hlv.models.Subject;

import java.util.List;

public interface SubjectRepository extends AppRepository<Subject, Long> {
    List<Subject> findByCareerId(Long careerId);
    boolean existsByCode(String code);
    List<Subject> findByCareerIdAndSemester(Long careerId, Integer semester);
}
