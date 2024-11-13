package com._a.backend.dtos.requests;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DoctorOfficeRequestDTO {
  private String specialization;
  private LocalDate startDate;
  private LocalDate endDate;
  private Long doctorId;
  private Long medicalFacilityId;
  private Long serviceUnitId;
  private Boolean isDelete;
}
