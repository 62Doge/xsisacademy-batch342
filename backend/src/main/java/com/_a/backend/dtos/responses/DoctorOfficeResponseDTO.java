package com._a.backend.dtos.responses;

import java.time.LocalDate;

import lombok.Data;

@Data
public class DoctorOfficeResponseDTO {
  private Long id;
  private String specialization;
  private LocalDate startDate;
  private LocalDate endDate;
  private Long doctorId;
  private Long medicalFacilityId;
  private Long serviceUnitId;
  private Boolean isDelete;
  private MedicalFacilityResponseDTO  medicalFacility;
  
}
