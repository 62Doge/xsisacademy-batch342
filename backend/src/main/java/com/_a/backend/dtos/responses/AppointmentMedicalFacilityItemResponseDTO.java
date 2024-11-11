package com._a.backend.dtos.responses;

import com._a.backend.entities.MedicalFacility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentMedicalFacilityItemResponseDTO {
  private Long id;
  private String name;

  public AppointmentMedicalFacilityItemResponseDTO(MedicalFacility medicalFacility) {
    this.id = medicalFacility.getId();
    this.name = medicalFacility.getName();
  }
}
