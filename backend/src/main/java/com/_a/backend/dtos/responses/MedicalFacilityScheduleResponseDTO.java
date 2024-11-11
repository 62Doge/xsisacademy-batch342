package com._a.backend.dtos.responses;

import java.util.List;
import java.util.stream.Collectors;

import com._a.backend.entities.MedicalFacilitySchedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalFacilityScheduleResponseDTO {
  private Long id;
  private String day;
  private String timeScheduleStart;
  private String timeScheduleEnd;
  private List<DoctorOfficeScheduleResponseDTO> doctorOfficeSchedules;

  public MedicalFacilityScheduleResponseDTO(MedicalFacilitySchedule facilitySchedule) {
    this.id = facilitySchedule.getId();
    this.day = facilitySchedule.getDay();
    this.timeScheduleStart = facilitySchedule.getTimeScheduleStart();
    this.timeScheduleEnd = facilitySchedule.getTimeScheduleEnd();

    this.doctorOfficeSchedules = facilitySchedule.getDoctorOfficeSchedules()
        .stream()
        .map(doctorOffice -> new DoctorOfficeScheduleResponseDTO(doctorOffice))
        .collect(Collectors.toList());
  }
}
