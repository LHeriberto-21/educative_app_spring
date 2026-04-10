package com.educative.app.hlv.dto;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequest {
    @NotBlank(message = "El nombre de la materia es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar 150 caracteres")
    private String name;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código no puede superar 20 caracteres")
    private String code;

    @Size(max = 300, message = "La descripción no puede superar 300 caracteres")
    private String description;

    @NotNull(message = "Los créditos son obligatorios")
    @Min(value = 1, message = "La materia debe tener al menos 1 crédito")
    private Integer credits;

    @NotNull(message = "El semestre es obligatorio")
    @Min(value = 1, message = "El semestre debe ser al menos 1")
    private Integer semester;

    @NotNull(message = "El máximo de alumnos es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 lugar disponible")
    @Max(value = 200, message = "No puede superar 200 alumnos")
    private Integer maxStudents;

    @NotNull(message = "La carrera es obligatoria")
    private Long careerId;

    // Opcional: puede asignarse después
    private Long teacherId;
}
