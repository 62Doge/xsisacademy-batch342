package com._a.backend.dtos.responses;

import com._a.backend.entities.DoctorOfficeSchedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorOfficeScheduleResponseDTO {
  private Long medicalFacilityId;
  private Long id;
  private int slot;
  private String day;
  private String timeScheduleStart;
  private String timeScheduleEnd;
  private Long medicalFacilityScheduleId;

  public DoctorOfficeScheduleResponseDTO(DoctorOfficeSchedule officeSchedule) {
    this.id = officeSchedule.getId();
    this.slot = officeSchedule.getSlot();
    this.day = officeSchedule.getMedicalFacilitySchedule().getDay();
    this.timeScheduleStart = officeSchedule.getMedicalFacilitySchedule().getTimeScheduleStart();
    this.timeScheduleEnd = officeSchedule.getMedicalFacilitySchedule().getTimeScheduleEnd();
    this.medicalFacilityScheduleId = officeSchedule.getMedicalFacilitySchedule().getId();

    this.medicalFacilityId = officeSchedule.getMedicalFacilitySchedule().getMedicalFacility().getId();
  }
}
