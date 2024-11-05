package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class CurrentDoctorSpecializationRequestDTO {
  private Long doctorId;
  private Long specializationId;
}
