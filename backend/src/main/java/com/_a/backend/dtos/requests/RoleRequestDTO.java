package com._a.backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleRequestDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String code;
}
