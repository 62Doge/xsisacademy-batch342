package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class DoctorEducationResponseDTO {
  private Long id;
  private String institutionName;
  private String major;
  private Integer startYear;
  private Integer endYear;
  private Boolean isLastEducation;
  private Long doctorId;
  private Long educationLevelId;
  private Boolean isDelete;
}
