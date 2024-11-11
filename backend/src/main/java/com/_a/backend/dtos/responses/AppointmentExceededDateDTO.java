package com._a.backend.dtos.responses;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppointmentExceededDateDTO {
  private Long count;
  private LocalDate appointmentDate;

  public AppointmentExceededDateDTO(Long count, LocalDate appointmentDate) {
    this.count = count;
    this.appointmentDate = appointmentDate;
  }
}
