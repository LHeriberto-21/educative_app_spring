package com.educative.app.hlv.dto.activity;

import com.educative.app.hlv.enums.ActivityType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class ActivityRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "La fecha límite es obligatoria")
    @Future(message = "La fecha límite debe ser en el futuro")
    private LocalDateTime dueDate;

    @NotNull(message = "La puntuación máxima es obligatoria")
    @DecimalMin(value = "1.0")
    @DecimalMax(value = "100.0")
    private Double maxScore;

    @NotNull(message = "El tipo es obligatorio")
    private ActivityType type;

    @NotNull(message = "La materia es obligatoria")
    private Long subjectId;

    @NotNull(message = "El semestre es obligatorio")
    private Integer semester;
}
