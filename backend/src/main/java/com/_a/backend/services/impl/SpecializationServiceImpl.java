package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.SpecializationRequestDTO;
import com._a.backend.dtos.responses.SpecializationResponseDTO;
import com._a.backend.entities.Specialization;
import com._a.backend.repositories.SpecializationRepository;
import com._a.backend.services.Services;

@Service
public class SpecializationServiceImpl implements Services<SpecializationRequestDTO, SpecializationResponseDTO> {

  @Autowired
  private SpecializationRepository specializationRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public Page<SpecializationResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAll'");
  }

  @Override
  public List<SpecializationResponseDTO> findAll() {
    List<Specialization> specializations = specializationRepository.findAll();
    List<SpecializationResponseDTO> specializationResponseDTOs = specializations.stream().map(
      specialization -> modelMapper.map(specialization, SpecializationResponseDTO.class)).toList();
    return specializationResponseDTOs;
}

  @Override
  public Optional<SpecializationResponseDTO> findById(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findById'");
  }

  @Override
  public SpecializationResponseDTO save(SpecializationRequestDTO specializationRequestDTO) {
    Specialization specialization = specializationRepository.
      save(modelMapper.map(specializationRequestDTO, Specialization.class));
    SpecializationResponseDTO specializationResponseDTO = modelMapper.map(
      specialization, SpecializationResponseDTO.class);
    return specializationResponseDTO;
    }

  @Override
  public SpecializationResponseDTO update(SpecializationRequestDTO specializationRequestDTO, Long id) {
    Optional<Specialization> specialization = specializationRepository.findById(id);
    if (specialization.isPresent()) {
      Specialization specialization1 = specialization.get();
      modelMapper.map(specializationRequestDTO, specialization1);
      Specialization specialization2 = specializationRepository.save(specialization1);
      return modelMapper.map(specialization2, SpecializationResponseDTO.class);
    }
    throw new RuntimeException("Specialization not found");
  }

  @Override
  public void deleteById(Long id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
  }
  
}
