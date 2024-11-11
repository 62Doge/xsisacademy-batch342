package com._a.backend.dtos.responses;

import java.util.Date;

import lombok.Data;

@Data
public class AppointmentResponseDTO {
  private Long Id;
  private Long customerId;
  private Long doctorOfficeId;
  private DoctorOfficeResponseDTO doctorOffice;
  private Long doctorOfficeScheduleId;
  private Long doctorOfficeTreatmentId;
  private Date appointmentDate;
}
