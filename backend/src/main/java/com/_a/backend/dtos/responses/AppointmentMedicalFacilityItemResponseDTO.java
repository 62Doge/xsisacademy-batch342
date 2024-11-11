package com._a.backend.dtos.responses;

import java.util.List;

import com._a.backend.entities.DoctorOffice;
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

  private List<TreatmentResponseDTO> treatments;

  public AppointmentMedicalFacilityItemResponseDTO(DoctorOffice medicalFacility,
      List<TreatmentResponseDTO> treatments) {
    this.id = medicalFacility.getId();
    this.name = medicalFacility.getName();

    this.treatments = treatments;
  }
}
