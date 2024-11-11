package com._a.backend.services;

import java.util.List;
import java.util.Optional;

import com._a.backend.dtos.projections.AppointmentExceededDateProjectionDTO;
import com._a.backend.dtos.responses.AppointmentExceededDateDTO;
import com._a.backend.dtos.responses.AppointmentMedicalFacilitiesResponseDTO;
import com._a.backend.dtos.responses.AppointmentResponseDTO;
import com._a.backend.dtos.responses.DoctorOfficeScheduleResponseDTO;

public interface AppointmentService {
  AppointmentMedicalFacilitiesResponseDTO getMedicalFacilitiesByDoctorId(Long id);

  List<AppointmentExceededDateProjectionDTO> getExceededDatesByDoctorId(Long doctorId);

  List<DoctorOfficeScheduleResponseDTO> getDoctorOfficeSchedulesByDoctorId(Long doctorId);

  List<AppointmentResponseDTO> findAll();

  Optional<AppointmentResponseDTO> findById(Long id);

  List<AppointmentResponseDTO> findByOfficeId(Long id);
}
