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
public class GradeRequest {

    @DecimalMin(value = "0.0", message = "La calificación no puede ser negativa")
    @DecimalMax(value = "100.0", message = "La calificación no puede superar 100")
    private Double partialGrade1;

    @DecimalMin(value = "0.0", message = "La calificación no puede ser nega tiva")
    @DecimalMax(value = "100.0", message = "La calificación no puede superar 100")
    private Double partialGrade2;

    @DecimalMin(value = "0.0", message = "La calificación no puede ser negativa")
    @DecimalMax(value = "100.0", message = "La calificación no puede superar 100")
    private Double finalGrade;

    @Size(max = 400, message = "Las observaciones no pueden superar 400 caracteres")
    private String observations;
}
