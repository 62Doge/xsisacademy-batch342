package com._a.backend.dtos.responses;

import com._a.backend.entities.DoctorOfficeTreatment;
import com._a.backend.entities.DoctorTreatment;

import lombok.Data;

@Data
public class TreatmentResponseDTO {
  private Long doctorOfficeTreatmentId;
  private String name;

  public TreatmentResponseDTO(DoctorTreatment doctorOfficeTreatment) {
    this.doctorOfficeTreatmentId = doctorOfficeTreatment.getId();
    this.name = name;
  }
}
