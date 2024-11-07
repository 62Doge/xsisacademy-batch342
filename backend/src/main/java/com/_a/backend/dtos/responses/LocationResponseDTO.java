package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class LocationResponseDTO {
    private Long id;
    private String name;
    private LocationResponseDTO parent;
    private Long parentId;
    private LocationLevelResponseDTO locationLevel;
    private Long locationLevelId;
}
