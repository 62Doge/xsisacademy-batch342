package com._a.backend.dtos.responses;

import java.util.List;

import lombok.Data;

@Data
public class AppointmentMedicalFacilitiesResponseDTO {
  private List<AppointmentMedicalFacilityItemResponseDTO> medicalFacilities;
}
