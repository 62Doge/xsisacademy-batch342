package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class DoctorEducationRequestDTO {
  private String institutionName;
  private String major;
  private Integer startYear;
  private Integer endYear;
  private Boolean isLastEducation;
  private Long doctorId;
  private Long educationLevelId;
}
