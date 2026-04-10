package com.educative.app.hlv.dto.activity;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class SubmissionRequest {

    @NotNull(message = "La actividad es obligatoria")
    private Long activityId;

    @NotNull(message = "El alumno es obligatorio")
    private Long studentId;
}
