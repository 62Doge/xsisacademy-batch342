package com._a.backend.dtos.projections;

import java.time.LocalDate;

public interface ExceedingAppoinmentProjectionDto {
  LocalDate getAppointmentDate();

  Long getMedicalFacilityId();

  Long getMedicalFacilityScheduleId();

  Long getAppointmentCount();
}
