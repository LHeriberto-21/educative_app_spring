package com.educative.app.hlv.repo;

import com.educative.app.hlv.enums.EnrollmentStatus;
import com.educative.app.hlv.models.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByStudentIdAndStatus(Long id, EnrollmentStatus status);
    List<Enrollment> findBySubjectId(Long subjectId);
    boolean existsByStudentIdAndSubjectId(Long studentId, Long subjectId);

    @Query("SELECT COUNT(e) FROM Enrollment e WHERE e.subject.id = :subjectId AND e.status = 'ACTIVE'")
    long countActiveEnrollmentsBySubject(@Param("subjectId") Long subjectId);


}
