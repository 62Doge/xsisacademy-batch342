package com._a.backend.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.projections.AppointmentExceededDateProjectionDTO;
import com._a.backend.dtos.responses.AppointmentMedicalFacilitiesResponseDTO;
import com._a.backend.dtos.responses.AppointmentMedicalFacilityItemResponseDTO;
import com._a.backend.dtos.responses.DoctorOfficeScheduleResponseDTO;
import com._a.backend.dtos.responses.AppointmentResponseDTO;
import com._a.backend.entities.Appointment;
import com._a.backend.entities.DoctorOffice;
import com._a.backend.repositories.AppointmentRepository;
import com._a.backend.repositories.DoctorOfficeRepository;
import com._a.backend.repositories.DoctorOfficeScheduleRepository;
import com._a.backend.services.AppointmentService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

  @Autowired
  DoctorOfficeRepository doctorOfficeRepository;

  @Autowired
  AppointmentRepository appointmentRepository;

  @Autowired
  DoctorOfficeScheduleRepository doctorOfficeScheduleRepository;

  @Autowired
  ModelMapper modelMapper;

  @Override
  public AppointmentMedicalFacilitiesResponseDTO getMedicalFacilitiesByDoctorId(Long id) {
    AppointmentMedicalFacilitiesResponseDTO responseDTO = new AppointmentMedicalFacilitiesResponseDTO();
    List<DoctorOffice> doctorOffice = doctorOfficeRepository.findByDoctorIdJpql(id);
    responseDTO.setMedicalFacilities(
        doctorOffice
            .stream()
            .map(office -> new AppointmentMedicalFacilityItemResponseDTO(office))
            .collect(Collectors.toList()));

    return responseDTO;
  }

  @Override
  public List<DoctorOfficeScheduleResponseDTO> getDoctorOfficeSchedulesByDoctorId(Long doctorId) {

    return doctorOfficeScheduleRepository.findByDoctorId(doctorId)
        .stream()
        .map(dos -> new DoctorOfficeScheduleResponseDTO(dos))
        .toList();
  }

  @Override
  public List<AppointmentExceededDateProjectionDTO> getExceededDatesByDoctorId(Long doctorId) {
    LocalDate today = LocalDate.now();
    LocalDate toDateLater = today.plusWeeks(3);

    return appointmentRepository.findExceededDates(doctorId, toDateLater);
  }

  @Override
  public Boolean isAppointmentCountExceeded(LocalDate appointmenDate, Long doctorOfficeScheduleId) {
    return appointmentRepository.isAppointmentCountExceeded(appointmenDate, doctorOfficeScheduleId).orElse(false);
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
