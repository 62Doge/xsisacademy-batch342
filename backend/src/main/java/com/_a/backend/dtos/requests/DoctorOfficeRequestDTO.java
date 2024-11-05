package com._a.backend.dtos.requests;

import java.util.Date;

import lombok.Data;

@Data
public class DoctorOfficeRequestDTO {
  private String specialization;
  private Date startDate;
  private Date endDate;
  private Long doctorId;
  private Long medicalFacilityId;
  private Long serviceUnitId;
  private Boolean isDelete;
}
