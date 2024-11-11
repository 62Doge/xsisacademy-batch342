package com._a.backend.dtos.requests;

import java.util.Date;

import lombok.Data;

@Data
public class AppointmentRequestDTO {
  private Long customerId;
  private Long doctorOfficeId;
  private Long doctorOfficeScheduleId;
  private Long doctorOfficeTreatmentId;
  private Date appointmentDate;
}
