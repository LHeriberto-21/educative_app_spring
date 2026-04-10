package com.educative.app.hlv.dto.activity;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class GradeSubmissionRequest {

    @NotNull(message = "La calificación es obligatoria")
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "100.0")
    private Double score;

    @Size(max = 500)
    private String feedback;
}
