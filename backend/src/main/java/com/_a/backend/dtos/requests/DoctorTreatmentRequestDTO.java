package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class DoctorTreatmentRequestDTO {
  private String name;
  private Long doctorId;
}
