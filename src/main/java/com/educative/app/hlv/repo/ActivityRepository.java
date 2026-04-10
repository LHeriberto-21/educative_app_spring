package com.educative.app.hlv.repo;

import com.educative.app.hlv.models.Activity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends AppRepository<Activity, Long> {

    List<Activity> findBySubjectIdAndActiveTrue(Long subjectId);

    // Actividades de una materia por semestre
    List<Activity> findBySubjectIdAndSemesterAndActiveTrue(Long subjectId, Integer semester);

    // Actividades creadas por un maestro
    List<Activity> findByCreatedByIdAndActiveTrue(Long teacherId);

    // Actividades próximas a vencer (para el recordatorio)
    // Busca actividades cuya dueDate esté entre ahora y las próximas 24 horas
    @Query("SELECT a FROM Activity a WHERE a.active = true AND a.dueDate BETWEEN :now AND :deadline")
    List<Activity> findUpcomingDeadlines(
            @Param("now") LocalDateTime now,
            @Param("deadline") LocalDateTime deadline
    );
}
