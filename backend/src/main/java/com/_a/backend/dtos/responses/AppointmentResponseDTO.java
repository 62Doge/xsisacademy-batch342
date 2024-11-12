package com._a.backend.dtos.responses;

import java.time.LocalDate;

import com._a.backend.entities.Appointment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppointmentResponseDTO {
  private Long id;

  private LocalDate appointmentDate;

  private DoctorOfficeResponseDTO doctorOffice;

  private Long customerId;

  private Long doctorOfficeId;

  private Long doctorOfficeScheduleId;

  private Long doctorOfficeTreatmentId;

  public AppointmentResponseDTO(Appointment appointment) {
    this.id = appointment.getId();
    this.appointmentDate = appointment.getAppointmentDate();
    this.customerId = appointment.getCustomerId();
    this.doctorOfficeId = appointment.getDoctorOfficeId();
    this.doctorOfficeScheduleId = appointment.getDoctorOfficeScheduleId();
    this.doctorOfficeTreatmentId = appointment.getDoctorOfficeTreatmentId();
  }
}
