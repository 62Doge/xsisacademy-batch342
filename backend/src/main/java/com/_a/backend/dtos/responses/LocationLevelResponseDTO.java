package com._a.backend.dtos.responses;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LocationLevelResponseDTO {
    private Long id;
    private String name;
    private String abbreviation;
    private LocalDate createdOn;
}
