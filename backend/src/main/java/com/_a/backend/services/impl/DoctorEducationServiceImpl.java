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

import com._a.backend.dtos.requests.DoctorEducationRequestDTO;
import com._a.backend.dtos.responses.DoctorEducationResponseDTO;
import com._a.backend.entities.DoctorEducation;
import com._a.backend.repositories.DoctorEducationRepository;
import com._a.backend.services.Services;

@Service
public class DoctorEducationServiceImpl implements Services<DoctorEducationRequestDTO, DoctorEducationResponseDTO> {

  @Autowired
  private DoctorEducationRepository doctorEducationRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public Page<DoctorEducationResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
    Sort sort = Sort.by(sortBy);
    sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    Page<DoctorEducation> doctorEducations = doctorEducationRepository.findAllByIsDeleteFalse(pageable);
    Page<DoctorEducationResponseDTO> doctorEducationResponseDTOS = doctorEducations
        .map(doctorEducation -> modelMapper.map(doctorEducation, DoctorEducationResponseDTO.class));
    return doctorEducationResponseDTOS;
  }

  @Override
  public List<DoctorEducationResponseDTO> findAll() {
    List<DoctorEducation> doctorEducations = doctorEducationRepository.findAll();
    List<DoctorEducationResponseDTO> doctorEducationResponseDTOs = doctorEducations.stream().map(
        doctorEducation -> modelMapper.map(doctorEducation, DoctorEducationResponseDTO.class)).toList();
    return doctorEducationResponseDTOs;
  }

  @Override
  public Optional<DoctorEducationResponseDTO> findById(Long id) {
    Optional<DoctorEducation> doctorEducation = doctorEducationRepository.findById(id);
    if (doctorEducation.isPresent()) {
      Optional<DoctorEducationResponseDTO> doctorEducationResponseDTO = doctorEducation.map(
          doctorEducationOptional -> modelMapper.map(doctorEducationOptional, DoctorEducationResponseDTO.class));
      return doctorEducationResponseDTO;
    }
    return Optional.empty();
  }

  public List<DoctorEducationResponseDTO> findByDocterId(Long id) {
    List<DoctorEducation> doctorEducations = doctorEducationRepository.findByDoctorId(id);
    List<DoctorEducationResponseDTO> doctorEducationResponseDTOs = doctorEducations.stream().map(
        doctorEducation -> modelMapper.map(doctorEducation, DoctorEducationResponseDTO.class)).toList();
    return doctorEducationResponseDTOs;
  }

  @Override
  public DoctorEducationResponseDTO save(DoctorEducationRequestDTO doctorEducationRequestDto) {
    DoctorEducation doctorEducation = doctorEducationRepository
        .save(modelMapper.map(doctorEducationRequestDto, DoctorEducation.class));
    DoctorEducationResponseDTO doctorEducationResponseDTO = modelMapper.map(doctorEducation,
        DoctorEducationResponseDTO.class);
    return doctorEducationResponseDTO;
  }

  @Override
  public DoctorEducationResponseDTO update(DoctorEducationRequestDTO doctorEducationRequestDTO, Long id) {
    Optional<DoctorEducation> optionalDoctorEducation = doctorEducationRepository.findById(id);
    if (optionalDoctorEducation.isPresent()) {
      DoctorEducation doctorEducation = optionalDoctorEducation.get();
      modelMapper.map(doctorEducationRequestDTO, doctorEducation);
      DoctorEducation updatedDoctorEducation = doctorEducationRepository.save(doctorEducation);
      return modelMapper.map(updatedDoctorEducation, DoctorEducationResponseDTO.class);
    }
    throw new RuntimeException("Doctor Education not found");
  }

  @Override
  public void deleteById(Long id) {
    doctorEducationRepository.deleteById(id);
  }

  // soft delete
  public void softDeleteById(Long id) {
    Optional<DoctorEducation> optionalDoctorEducation = doctorEducationRepository.findById(id);
    if (optionalDoctorEducation.isPresent()) {
      DoctorEducation doctorEducation = optionalDoctorEducation.get();
      doctorEducation.setIsDelete(true);
      doctorEducation.setDeletedOn(LocalDateTime.now());
      doctorEducationRepository.save(doctorEducation);
    } else {
      throw new RuntimeException("Doctor Education not found");
    }
  }

}
