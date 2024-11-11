package com._a.backend.dtos.responses;

import com._a.backend.entities.DoctorOfficeTreatment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoctorOfficeTreatmentResponseDTO {
  private Long id;
  private String treatmentName;

  public DoctorOfficeTreatmentResponseDTO(DoctorOfficeTreatment officeTreatment) {
    this.id = officeTreatment.getId();
    this.treatmentName = officeTreatment.getDoctorTreatment().getName();
  }
}
