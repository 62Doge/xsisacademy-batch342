package com._a.backend.services.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._a.backend.dtos.projections.AppointmentExceededDateProjectionDTO;
import com._a.backend.dtos.requests.AppointmentRequestDTO;
import com._a.backend.dtos.responses.AppointmentCustomerDocterOfficeScheduleResponseDTO;
import com._a.backend.dtos.responses.AppointmentMedicalFacilitiesResponseDTO;
import com._a.backend.dtos.responses.AppointmentMedicalFacilityItemResponseDTO;
import com._a.backend.dtos.responses.AppointmentResponseDTO;
import com._a.backend.dtos.responses.DoctorOfficeScheduleResponseDTO;
import com._a.backend.entities.Appointment;
import com._a.backend.entities.Customer;
import com._a.backend.entities.DoctorOffice;
import com._a.backend.entities.DoctorOfficeSchedule;
import com._a.backend.entities.DoctorOfficeTreatment;
import com._a.backend.exceptions.IdNotFoundException;
import com._a.backend.repositories.AppointmentRepository;
import com._a.backend.repositories.CustomerRepository;
import com._a.backend.repositories.DoctorOfficeRepository;
import com._a.backend.repositories.DoctorOfficeScheduleRepository;
import com._a.backend.repositories.DoctorOfficeTreatmentRepository;
import com._a.backend.services.AppointmentService;
import com._a.backend.services.AuthService;

@Service
public class AppointmentServiceImpl implements AppointmentService {

  @Autowired
  DoctorOfficeRepository doctorOfficeRepository;

  @Autowired
  AppointmentRepository appointmentRepository;

  @Autowired
  DoctorOfficeScheduleRepository doctorOfficeScheduleRepository;

  @Autowired
  CustomerRepository customerRepository;

  @Autowired
  DoctorOfficeTreatmentRepository doctorOfficeTreatmentRepository;

  @Autowired
  AuthService authService;

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

  @Transactional
  @Override
  public AppointmentResponseDTO create(AppointmentRequestDTO requestDTO) {
    Customer customer = customerRepository.findById(requestDTO.getCustomerId())
        .orElseThrow(() -> new IdNotFoundException("Customer not found"));
    DoctorOffice doctorOffice = doctorOfficeRepository.findById(requestDTO.getDoctorOfficeId())
        .orElseThrow(() -> new IdNotFoundException("Doctor office not found"));
    DoctorOfficeSchedule doctorOfficeSchedule = doctorOfficeScheduleRepository
        .findById(requestDTO.getDoctorOfficeScheduleId())
        .orElseThrow(() -> new IdNotFoundException("Doctor office schedule not found"));
    DoctorOfficeTreatment doctorOfficeTreatment = doctorOfficeTreatmentRepository
        .findById(requestDTO.getDoctorOfficeTreatmentId())
        .orElseThrow(() -> new IdNotFoundException("Doctor office treatment not found"));

    Long userId = authService.getDetails().getId();

    if (this.isAppointmentCountExceeded(requestDTO.getAppointmentDate(), doctorOfficeSchedule.getId())) {
      throw new RuntimeException("Slot penuh!");
    }

    Appointment appointment = new Appointment();
    appointment.setAppointmentDate(requestDTO.getAppointmentDate());
    appointment.setCustomerId(customer.getId());
    appointment.setDoctorOfficeId(doctorOffice.getId());
    appointment.setDoctorOfficeScheduleId(doctorOfficeSchedule.getId());
    appointment.setDoctorOfficeTreatmentId(doctorOfficeTreatment.getId());
    appointment.setCreatedBy(userId);

    AppointmentResponseDTO responseDTO = new AppointmentResponseDTO(appointmentRepository.save(appointment));

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

  @Override
  public List<AppointmentCustomerDocterOfficeScheduleResponseDTO> getAllCustomerDoctorOfficeScheduleByUserId() {
    Long userId = authService.getDetails().getId();
    return appointmentRepository
        .findAllByUserId(userId)
        .stream()
        .map(appointment -> new AppointmentCustomerDocterOfficeScheduleResponseDTO(appointment))
        .toList();
  }
}
