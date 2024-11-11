package com._a.backend.dtos.responses;

import java.util.List;
import java.util.stream.Collectors;

import com._a.backend.entities.DoctorOffice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentMedicalFacilityItemResponseDTO {
  private Long id;
  private String name;
  private Long doctorOfficeId;

  private List<DoctorOfficeTreatmentResponseDTO> officeTreatments;

  public AppointmentMedicalFacilityItemResponseDTO(DoctorOffice doctorOffice) {
    this.id = doctorOffice.getMedicalFacility().getId();
    this.name = doctorOffice.getMedicalFacility().getName();
    this.doctorOfficeId = doctorOffice.getId();

    this.officeTreatments = doctorOffice
        .getDoctorOfficeTreatments()
        .stream()
        .map(doctorOfficeTreatment -> new DoctorOfficeTreatmentResponseDTO(doctorOfficeTreatment))
        .collect(Collectors.toList());
  }
}
