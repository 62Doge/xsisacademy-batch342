package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class LocationChildResponseDTO {
    private Long id;
    private String name;
    private Long locationLevelId;
}
