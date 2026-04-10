package com.educative.app.hlv.repo;

import com.educative.app.hlv.enums.SubmissionStatus;
import com.educative.app.hlv.models.ActivitySubmission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActivitySubmissionRepository extends AppRepository<ActivitySubmission, Long>{

    // Entregas de un alumno (todas sus actividades entregadas)
    List<ActivitySubmission> findByStudentId(Long studentId);

    // Entregas de una actividad (para que el maestro vea quién entregó)
    List<ActivitySubmission> findByActivityId(Long activityId);

    // Buscar la entrega específica de un alumno en una actividad
    Optional<ActivitySubmission> findByActivityIdAndStudentId(Long activityId, Long studentId);

    // Verificar si el alumno ya tiene una entrega para esta actividad
    boolean existsByActivityIdAndStudentId(Long activityId, Long studentId);

    // Entregas por estado (ej: todas las pendientes de una actividad)
    List<ActivitySubmission> findByActivityIdAndStatus(Long activityId, SubmissionStatus status);
}
