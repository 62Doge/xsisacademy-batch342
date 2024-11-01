package com._a.backend.dtos.responses;

import com._a.backend.entities.Location;
import com._a.backend.entities.LocationLevel;

import lombok.Data;

@Data
public class LocationResponseDTO {
    private Long id;
    private String name;
    private Location parent;
    private Long parentId;
    private LocationLevel locationLevel;
    private Long locationLevelId;
}
