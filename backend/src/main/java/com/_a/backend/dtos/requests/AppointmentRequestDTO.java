package com._a.backend.dtos.requests;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AppointmentRequestDTO {
  private LocalDate appointmentDate;

  private Long customerId;

  private Long doctorOfficeId;

  private Long doctorOfficeScheduleId;

  private Long doctorOfficeTreatmentId;
}
