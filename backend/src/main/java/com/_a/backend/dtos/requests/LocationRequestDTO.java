package com._a.backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LocationRequestDTO {
    @NotBlank(message = "Name is mandatory")
    private String name;
    private Long parentId;
    private Long locationLevelId;
    private Long createdBy;
    private Long modifiedBy;
    private Long deletedBy;
}
