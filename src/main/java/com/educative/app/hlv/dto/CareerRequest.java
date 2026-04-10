package com.educative.app.hlv.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CareerRequest {
    @NotBlank(message = "El nombre de la carrera es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String name;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código no puede superar 20 caracteres")
    private String code;

    @Size(max = 300, message = "La descripción no puede superar 300 caracteres")
    private String description;

    @NotNull(message = "El total de semestres es obligatorio")
    @Min(value = 1, message = "La carrera debe tener al menos 1 semestre")
    @Max(value = 20, message = "La carrera no puede tener más de 20 semestres")
    private Integer totalSemesters;
}
