package com._a.backend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.CurrentDoctorSpecializationRequestDTO;
import com._a.backend.dtos.responses.CurrentDoctorSpecializationResponseDTO;
import com._a.backend.entities.CurrentDoctorSpecialization;
import com._a.backend.repositories.CurrentDoctorSpecializationRepository;
import com._a.backend.services.Services;

@Service
public class CurrentDoctorSpecializationServiceImpl
    implements Services<CurrentDoctorSpecializationRequestDTO, CurrentDoctorSpecializationResponseDTO> {

  @Autowired
  private CurrentDoctorSpecializationRepository currentDoctorSpecializationRepository;

  @Autowired
  ModelMapper modelMapper;

  @Override
  public Page<CurrentDoctorSpecializationResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
    Sort sort = Sort.by(sortBy);
    sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    Page<CurrentDoctorSpecialization> currentdoctorSpecializations = currentDoctorSpecializationRepository.findAllByIsDeleteFalse(pageable);
    Page<CurrentDoctorSpecializationResponseDTO> currentdoctorSpecializationResponseDTOS = currentdoctorSpecializations
        .map(currentdoctorSpecialization -> modelMapper.map(currentdoctorSpecialization, CurrentDoctorSpecializationResponseDTO.class));
    return currentdoctorSpecializationResponseDTOS;
  }

  @Override
  public List<CurrentDoctorSpecializationResponseDTO> findAll() {
    List<CurrentDoctorSpecialization> currentDoctorSpecializations = currentDoctorSpecializationRepository.findAll();
    List<CurrentDoctorSpecializationResponseDTO> currentDoctorSpecializationResponseDTOs = currentDoctorSpecializations
        .stream().map(
            currentDoctorSpecialization -> modelMapper.map(currentDoctorSpecialization,
                CurrentDoctorSpecializationResponseDTO.class))
        .toList();
    return currentDoctorSpecializationResponseDTOs;
  }

  @Override
  public Optional<CurrentDoctorSpecializationResponseDTO> findById(Long id) {
    Optional<CurrentDoctorSpecialization> currentDoctorSpecialization = currentDoctorSpecializationRepository
        .findById(id);
    if (currentDoctorSpecialization.isPresent()) {
      Optional<CurrentDoctorSpecializationResponseDTO> currentDoctorSpecializationResponseDTO = currentDoctorSpecialization
          .map(
              currentDoctorSpecializationOptional -> modelMapper.map(currentDoctorSpecializationOptional,
                  CurrentDoctorSpecializationResponseDTO.class));
      return currentDoctorSpecializationResponseDTO;
    }
    return Optional.empty();
  }

  public List<CurrentDoctorSpecializationResponseDTO> findByDoctorId(Long id) {
    List<CurrentDoctorSpecialization> currentDoctorSpecializations = currentDoctorSpecializationRepository
        .findByDoctorId(id);
    List<CurrentDoctorSpecializationResponseDTO> currentDoctorSpecializationResponseDTOs = currentDoctorSpecializations
        .stream().map(
            currentDoctorSpecialization -> modelMapper.map(currentDoctorSpecialization,
                CurrentDoctorSpecializationResponseDTO.class))
        .toList();
    return currentDoctorSpecializationResponseDTOs;
  }

  @Override
  public CurrentDoctorSpecializationResponseDTO save(
      CurrentDoctorSpecializationRequestDTO currentDoctorSpecializationRequestDTO) {
    CurrentDoctorSpecialization currentDoctorSpecialization = currentDoctorSpecializationRepository
        .save(modelMapper.map(currentDoctorSpecializationRequestDTO, CurrentDoctorSpecialization.class));
    CurrentDoctorSpecializationResponseDTO currentDoctorSpecializationResponseDTO = modelMapper.map(
        currentDoctorSpecialization, CurrentDoctorSpecializationResponseDTO.class);
    return currentDoctorSpecializationResponseDTO;
  }
  
  @Override
  public CurrentDoctorSpecializationResponseDTO update(
      CurrentDoctorSpecializationRequestDTO currentDoctorSpecializationRequestDTO, Long id) {
    Optional<CurrentDoctorSpecialization> optionalCurrentDoctorSpecialization = currentDoctorSpecializationRepository
        .findById(id);
    if (optionalCurrentDoctorSpecialization.isPresent()) {
      CurrentDoctorSpecialization currentDoctorSpecialization = optionalCurrentDoctorSpecialization.get();
      modelMapper.map(currentDoctorSpecializationRequestDTO, currentDoctorSpecialization);
      CurrentDoctorSpecialization updatedCurrentDoctorSpecialization = currentDoctorSpecializationRepository.save(currentDoctorSpecialization);
      return modelMapper.map(updatedCurrentDoctorSpecialization, CurrentDoctorSpecializationResponseDTO.class);
    }
    throw new RuntimeException("Current Doctor Specialization not found");
  }

  @Override
  public void deleteById(Long id) {
    currentDoctorSpecializationRepository.deleteById(id);
  }
  
}
