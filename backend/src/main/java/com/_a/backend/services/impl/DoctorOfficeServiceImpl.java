package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.DoctorOfficeRequestDTO;
import com._a.backend.dtos.responses.DoctorOfficeResponseDTO;
import com._a.backend.entities.DoctorOffice;
import com._a.backend.repositories.DoctorOfficeRepository;
import com._a.backend.services.Services;

@Service
public class DoctorOfficeServiceImpl implements Services<DoctorOfficeRequestDTO, DoctorOfficeResponseDTO> {

  @Autowired
  private DoctorOfficeRepository doctorOfficeRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Override
  public Page<DoctorOfficeResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
    Sort sort = Sort.by(sortBy);
    sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    Page<DoctorOffice> doctorOffices = doctorOfficeRepository.findAllByIsDeleteFalse(pageable);
    Page<DoctorOfficeResponseDTO> doctorOfficeResponseDTOS = doctorOffices
        .map(doctorOffice -> modelMapper.map(doctorOffice, DoctorOfficeResponseDTO.class));
    return doctorOfficeResponseDTOS;
  }

  @Override
  public List<DoctorOfficeResponseDTO> findAll() {
    List<DoctorOffice> doctorOffices = doctorOfficeRepository.findAll();
    List<DoctorOfficeResponseDTO> doctorOfficeResponseDTOs = doctorOffices.stream()
        .map(doctorOffice -> modelMapper.map(doctorOffice, DoctorOfficeResponseDTO.class)).toList();
    return doctorOfficeResponseDTOs;
  }

  @Override
  public Optional<DoctorOfficeResponseDTO> findById(Long id) {
    Optional<DoctorOffice> doctorOffice = doctorOfficeRepository.findById(id);
    if (doctorOffice.isPresent()) {
      Optional<DoctorOfficeResponseDTO> doctorOfficeResponseDTO = doctorOffice.map(
          doctorOfficeOptional -> modelMapper.map(doctorOfficeOptional, DoctorOfficeResponseDTO.class));
      return doctorOfficeResponseDTO;
    }
    return Optional.empty();
  }

  public List<DoctorOfficeResponseDTO> findByDoctorId(Long id) {
    List<DoctorOffice> doctorOffices = doctorOfficeRepository.findByDoctorId(id);
    List<DoctorOfficeResponseDTO> doctorOfficeResponseDTOs = doctorOffices.stream().map(
        doctorOffice -> modelMapper.map(doctorOffice, DoctorOfficeResponseDTO.class)).toList();
    return doctorOfficeResponseDTOs;
  }

  @Override
  public DoctorOfficeResponseDTO save(DoctorOfficeRequestDTO doctorOfficeRequestDto) {
    DoctorOffice doctorOffice = doctorOfficeRepository
        .save(modelMapper.map(doctorOfficeRequestDto, DoctorOffice.class));
    DoctorOfficeResponseDTO doctorOfficeResponseDTO = modelMapper.map(
        doctorOffice, DoctorOfficeResponseDTO.class);
    return doctorOfficeResponseDTO;
  }

  @Override
  public DoctorOfficeResponseDTO update(DoctorOfficeRequestDTO doctorOfficeRequestDto, Long id) {
    Optional<DoctorOffice> optionalDoctorOffice = doctorOfficeRepository.findById(id);
    if (optionalDoctorOffice.isPresent()) {
      DoctorOffice doctorOffice = optionalDoctorOffice.get();
      modelMapper.map(doctorOfficeRequestDto, doctorOffice);
      DoctorOffice updatedDoctorOffice = doctorOfficeRepository.save(doctorOffice);
      return modelMapper.map(updatedDoctorOffice, DoctorOfficeResponseDTO.class);
    }
    throw new RuntimeException("Doctor Office not found");
  }

  @Override
  public void deleteById(Long id) {
    doctorOfficeRepository.deleteById(id);
  }

  public void softDeleteById(Long id) {
    Optional<DoctorOffice> optionalDoctorOffice = doctorOfficeRepository.findById(id);
    if (optionalDoctorOffice.isPresent()) {
      DoctorOffice doctorOffice = optionalDoctorOffice.get();
      doctorOffice.setIsDelete(true);
      doctorOfficeRepository.save(doctorOffice);
    } else {
      throw new RuntimeException("Doctor Office not found");
    }
  }

}
