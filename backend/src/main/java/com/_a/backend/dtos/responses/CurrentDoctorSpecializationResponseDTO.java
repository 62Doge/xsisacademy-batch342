package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class CurrentDoctorSpecializationResponseDTO {
  private Long id;
  private Long doctorId;
  private DoctorResponseDTO doctor;
  private Long specializationId;
  private SpecializationResponseDTO specialization;

}
