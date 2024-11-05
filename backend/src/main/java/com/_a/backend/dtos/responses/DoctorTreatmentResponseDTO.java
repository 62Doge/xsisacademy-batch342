package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class DoctorTreatmentResponseDTO {
  private Long id;
  private String name;
  private Long doctorId;
}
