package com._a.backend.dtos.projections;

import java.time.LocalDate;

public interface AppointmentExceededDateProjectionDTO {
  Long getAppointmentCount();

  LocalDate getAppointmentDate();

  Long getDoctorOfficeScheduleId();
}
