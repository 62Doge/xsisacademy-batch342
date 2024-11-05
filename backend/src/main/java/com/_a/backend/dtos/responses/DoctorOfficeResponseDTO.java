package com._a.backend.dtos.responses;

import java.util.Date;

import lombok.Data;

@Data
public class DoctorOfficeResponseDTO {
  private Long id;
  private String specialization;
  private Date startDate;
  private Date endDate;
  private Long doctorId;
  private Long medicalFacilityId;
  private Long serviceUnitId;
  private Boolean isDelete;
  private MedicalFacilityResponseDTO  medicalFacility;
  
}
