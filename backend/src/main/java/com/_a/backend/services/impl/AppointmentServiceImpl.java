package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.AppointmentRequestDTO;
import com._a.backend.dtos.responses.AppointmentResponseDTO;
import com._a.backend.entities.Appointment;
import com._a.backend.repositories.AppointmentRepository;
import com._a.backend.services.Services;

@Service
public class AppointmentServiceImpl implements Services<AppointmentRequestDTO, AppointmentResponseDTO>{

  @Autowired
  private AppointmentRepository appointmentRepository;

  @Autowired
  ModelMapper modelMapper;

  @Override
  public Page<AppointmentResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAll'");
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

  public List<AppointmentResponseDTO> findByOfficeId(Long id) {
    List<Appointment> appointments = appointmentRepository.findByOfficeId(id);
    List<AppointmentResponseDTO> appointmentResponseDTOs = appointments.stream().map(
        appointment -> modelMapper.map(appointment, AppointmentResponseDTO.class)).toList();
    return appointmentResponseDTOs;
  }

  @Override
  public AppointmentResponseDTO save(AppointmentRequestDTO appointmentRequestDTO) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'save'");
  }

  @Override
  public AppointmentResponseDTO update(AppointmentRequestDTO appointmentRequestDTO, Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'update'");
  }

  @Override
  public void deleteById(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
  }
  
}
