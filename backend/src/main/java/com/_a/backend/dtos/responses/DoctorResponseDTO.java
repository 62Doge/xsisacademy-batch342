package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class DoctorResponseDTO {
  private Long id;
  private Long biodataId;
  private String str;
  private BiodataResponseDTO biodata;
}
