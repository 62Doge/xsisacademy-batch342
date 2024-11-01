package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class LocationRequestDTO {
    private String name;
    private Long parentId;
    private Long locationLevelId;
}
