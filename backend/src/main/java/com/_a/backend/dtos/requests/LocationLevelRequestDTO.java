package com._a.backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LocationLevelRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;
    private String abbreviation;
}
