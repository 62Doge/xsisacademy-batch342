package com._a.backend.services.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

  @Autowired
  private DumpAuthServiceImpl dumpAuthServiceImpl;

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
  public CurrentDoctorSpecializationResponseDTO save(CurrentDoctorSpecializationRequestDTO currentDoctorSpecializationRequestDTO) {
    CurrentDoctorSpecialization currentDoctorSpecialization = modelMapper.map(currentDoctorSpecializationRequestDTO, CurrentDoctorSpecialization.class);
    currentDoctorSpecialization.setCreatedBy(dumpAuthServiceImpl.getDetails().getId());
    currentDoctorSpecialization = currentDoctorSpecializationRepository.save(currentDoctorSpecialization);
    CurrentDoctorSpecializationResponseDTO currentDoctorSpecializationResponseDTO = modelMapper.map(currentDoctorSpecialization, CurrentDoctorSpecializationResponseDTO.class);
    return currentDoctorSpecializationResponseDTO;
  }
  
  @Override
  public CurrentDoctorSpecializationResponseDTO update(
      CurrentDoctorSpecializationRequestDTO currentDoctorSpecializationRequestDTO, Long id) {
    List<CurrentDoctorSpecialization> currentDoctorSpecializations = currentDoctorSpecializationRepository.findByDoctorId(id);
    if (currentDoctorSpecializations.isEmpty()) {
        throw new NoSuchElementException("Specialization for the doctor not found");
    }
    CurrentDoctorSpecialization currentDoctorSpecialization = currentDoctorSpecializations.get(0);
    currentDoctorSpecialization.setModifiedBy(dumpAuthServiceImpl.getDetails().getId());
    modelMapper.map(currentDoctorSpecializationRequestDTO, currentDoctorSpecialization);
    CurrentDoctorSpecialization updatedCurrentDoctorSpecialization = currentDoctorSpecializationRepository.save(currentDoctorSpecialization);
    return modelMapper.map(updatedCurrentDoctorSpecialization, CurrentDoctorSpecializationResponseDTO.class);
    
  }
 
  @Override
  public void deleteById(Long id) {
    currentDoctorSpecializationRepository.deleteById(id);
  }
  
}
