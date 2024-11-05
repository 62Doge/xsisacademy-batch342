package com._a.backend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.DoctorTreatmentRequestDTO;
import com._a.backend.dtos.responses.DoctorTreatmentResponseDTO;
import com._a.backend.entities.DoctorTreatment;
import com._a.backend.repositories.DoctorTreatmentRepository;
import com._a.backend.services.Services;

@Service
public class DoctorTreatmentServiceImpl implements Services<DoctorTreatmentRequestDTO, DoctorTreatmentResponseDTO> {

  @Autowired
  private DoctorTreatmentRepository doctorTreatmentRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public Page<DoctorTreatmentResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
    Sort sort = Sort.by(sortBy);
    sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    Page<DoctorTreatment> doctorTreatments = doctorTreatmentRepository.findAllByIsDeleteFalse(pageable);
    Page<DoctorTreatmentResponseDTO> doctorTreatmentResponseDTOS = doctorTreatments
        .map(doctorTreatment -> modelMapper.map(doctorTreatment, DoctorTreatmentResponseDTO.class));
    return doctorTreatmentResponseDTOS;
  }

  @Override
  public List<DoctorTreatmentResponseDTO> findAll() {
    List<DoctorTreatment> doctorTreatments = doctorTreatmentRepository.findAll();
    List<DoctorTreatmentResponseDTO> doctorTreatmentResponseDTOs = doctorTreatments.stream().map(
        doctorTreatment -> modelMapper.map(doctorTreatment, DoctorTreatmentResponseDTO.class)).toList();
    return doctorTreatmentResponseDTOs;
  }

  @Override
  public Optional<DoctorTreatmentResponseDTO> findById(Long id) {
    Optional<DoctorTreatment> doctorTreatment = doctorTreatmentRepository.findById(id);
    if (doctorTreatment.isPresent()) {
      Optional<DoctorTreatmentResponseDTO> doctorTreatmentResponseDTO = doctorTreatment.map(
          doctorTreatmentOptional -> modelMapper.map(doctorTreatmentOptional, DoctorTreatmentResponseDTO.class));
      return doctorTreatmentResponseDTO;
    }
    return Optional.empty();
  }

  public List<DoctorTreatmentResponseDTO> findByDocterId(Long id) {
    List<DoctorTreatment> doctorTreatments = doctorTreatmentRepository.findByDoctorId(id);
    List<DoctorTreatmentResponseDTO> doctorTreatmentResponseDTOs = doctorTreatments.stream().map(
        doctorTreatment -> modelMapper.map(doctorTreatment, DoctorTreatmentResponseDTO.class)).toList();
    return doctorTreatmentResponseDTOs;
  }

  @Override
  public DoctorTreatmentResponseDTO save(DoctorTreatmentRequestDTO doctorTreatmentRequestDTO) {
    DoctorTreatment doctorTreatment = doctorTreatmentRepository
        .save(modelMapper.map(doctorTreatmentRequestDTO, DoctorTreatment.class));
    DoctorTreatmentResponseDTO doctorTreatmentResponseDTO = modelMapper.map(doctorTreatment,
        DoctorTreatmentResponseDTO.class);
    return doctorTreatmentResponseDTO;
  }

  @Override
  public DoctorTreatmentResponseDTO update(DoctorTreatmentRequestDTO doctorTreatmentRequestDTO, Long id) {
    Optional<DoctorTreatment> optionalDoctorTreatment = doctorTreatmentRepository.findById(id);
    if (optionalDoctorTreatment.isPresent()) {
      DoctorTreatment doctorTreatment = optionalDoctorTreatment.get();
      modelMapper.map(doctorTreatmentRequestDTO, doctorTreatment);
      DoctorTreatment updatedDoctorTreatment = doctorTreatmentRepository.save(doctorTreatment);
      return modelMapper.map(updatedDoctorTreatment, DoctorTreatmentResponseDTO.class);
    }
    throw new RuntimeException("DoctorTreatment not found");
  }

  @Override
  public void deleteById(Long id) {
    doctorTreatmentRepository.deleteById(id);
  }

  // soft Delete
  public void softDeleteById(Long id) {
    Optional<DoctorTreatment> optionalDoctorTreatment = doctorTreatmentRepository.findById(id);
    if (optionalDoctorTreatment.isPresent()) {
      DoctorTreatment doctorTreatment = optionalDoctorTreatment.get();
      doctorTreatment.setIsDelete(true);
      doctorTreatment.setDeletedOn(LocalDateTime.now());
      doctorTreatmentRepository.save(doctorTreatment);
    } else {
      throw new RuntimeException("Doctor Treatment not found");
    }
  }
}
