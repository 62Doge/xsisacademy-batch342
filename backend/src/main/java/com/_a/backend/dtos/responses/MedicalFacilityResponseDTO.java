package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class MedicalFacilityResponseDTO {
  private String name;
  private LocationResponseDTO location;
}
