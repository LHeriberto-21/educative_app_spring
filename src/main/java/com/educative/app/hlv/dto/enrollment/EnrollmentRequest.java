package com.educative.app.hlv.dto.enrollment;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {
    @NotNull(message = "El alumno es obligatorio")
    private Long studentId;

    @NotNull(message = "La materia es obligatoria")
    private Long subjectId;
}
