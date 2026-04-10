package com.educative.app.hlv.dto.activity;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateProfileRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String lastname;
}
