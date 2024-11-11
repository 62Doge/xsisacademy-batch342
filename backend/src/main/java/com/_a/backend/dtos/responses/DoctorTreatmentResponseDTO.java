package com._a.backend.dtos.responses;

import com._a.backend.entities.DoctorTreatment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DoctorTreatmentResponseDTO {
  private Long id;
  private String name;
  private Long doctorId;

  public DoctorTreatmentResponseDTO(DoctorTreatment doctorTreatment) {
    this.id = doctorTreatment.getId();
    this.name = doctorTreatment.getName();
    this.doctorId = doctorTreatment.getDoctorId();
  }
}
