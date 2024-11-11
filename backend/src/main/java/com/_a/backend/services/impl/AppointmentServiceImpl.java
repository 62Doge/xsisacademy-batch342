package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.responses.AppointmentMedicalFacilitiesResponseDTO;
import com._a.backend.dtos.responses.AppointmentMedicalFacilityItemResponseDTO;
import com._a.backend.dtos.responses.AppointmentResponseDTO;
import com._a.backend.entities.Appointment;
import com._a.backend.repositories.AppointmentRepository;
import com._a.backend.repositories.DoctorOfficeRepository;
import com._a.backend.services.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

  @Autowired
  DoctorOfficeRepository doctorOfficeRepository;

  @Autowired
  private AppointmentRepository appointmentRepository;

  @Autowired
  ModelMapper modelMapper;

  @Override
  public AppointmentMedicalFacilitiesResponseDTO getMedicalFacilitiesByDoctorId(Long id) {
    AppointmentMedicalFacilitiesResponseDTO responseDTO = new AppointmentMedicalFacilitiesResponseDTO();
    responseDTO.setMedicalFacilities(doctorOfficeRepository.findMedicalFacilitiesByDoctorId(id)
        .stream()
        .map(medicalFacility -> new AppointmentMedicalFacilityItemResponseDTO(medicalFacility))
        .collect(Collectors.toList()));

    return responseDTO;
  }

  @Override
  public List<AppointmentResponseDTO> findAll() {
    List<Appointment> appointments = appointmentRepository.findAll();
    List<AppointmentResponseDTO> appointmentResponseDTOs = appointments.stream().map(
        appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class)).toList();
    return appointmentResponseDTOs;
  }

  @Override
  public Optional<AppointmentResponseDTO> findById(Long id) {
    Optional<Appointment> appointment = appointmentRepository.findById(id);
    if (appointment.isPresent()) {
      Optional<AppointmentResponseDTO> appointmentResponseDTO = appointment.map(
          appointmentOptional -> modelMapper.map(appointmentOptional, AppointmentResponseDTO.class));
      return appointmentResponseDTO;
    }
    return Optional.empty();
  }

  @Override
  public List<AppointmentResponseDTO> findByOfficeId(Long id) {
    List<Appointment> appointments = appointmentRepository.findByOfficeId(id);
    List<AppointmentResponseDTO> appointmentResponseDTOs = appointments.stream().map(
        appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class)).toList();
    return appointmentResponseDTOs;
  }
}
