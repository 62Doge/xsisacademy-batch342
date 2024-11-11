package com._a.backend.services;

import java.util.List;
import java.util.Optional;

import com._a.backend.dtos.responses.AppointmentMedicalFacilitiesResponseDTO;
import com._a.backend.dtos.responses.AppointmentResponseDTO;

public interface AppointmentService {
  AppointmentMedicalFacilitiesResponseDTO getMedicalFacilitiesByDoctorId(Long id);

  List<AppointmentResponseDTO> findAll();

  Optional<AppointmentResponseDTO> findById(Long id);

  List<AppointmentResponseDTO> findByOfficeId(Long id);
}
