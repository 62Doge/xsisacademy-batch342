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

import com._a.backend.dtos.requests.DoctorRequestDTO;
import com._a.backend.dtos.responses.DoctorResponseDTO;
import com._a.backend.entities.Doctor;
import com._a.backend.repositories.DoctorRepository;
import com._a.backend.services.Services;

@Service
public class DoctorServiceImpl implements Services<DoctorRequestDTO, DoctorResponseDTO> {

  @Autowired
  private DoctorRepository doctorRepository;

  @Autowired
  ModelMapper modelMapper;

  @Override
  public Page<DoctorResponseDTO> getAll(int pageNo, int pageSize, String sortBy,
      String sortDirection) {
    Sort sort = Sort.by(sortBy);
    sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    Page<Doctor> doctors = doctorRepository
        .findAllByIsDeleteFalse(pageable);
    Page<DoctorResponseDTO> doctorResponseDTOS = doctors
        .map(doctor -> modelMapper.map(doctor,
            DoctorResponseDTO.class));
    return doctorResponseDTOS;
  }

  @Override
  public List<DoctorResponseDTO> findAll() {
    List<Doctor> doctors = doctorRepository.findAll();
    List<DoctorResponseDTO> doctorResponseDTOs = doctors.stream().map(
        doctor -> modelMapper.map(doctor, DoctorResponseDTO.class)).toList();
    return doctorResponseDTOs;
  }

  @Override
  public Optional<DoctorResponseDTO> findById(Long id) {
    Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
    if (optionalDoctor.isPresent()) {
      Optional<DoctorResponseDTO> doctorResponseDTO = optionalDoctor.map(
          doctorOptional -> modelMapper.map(doctorOptional, DoctorResponseDTO.class));
      return doctorResponseDTO;
    }
    return Optional.empty();
  }

  @Override
  public DoctorResponseDTO save(DoctorRequestDTO doctorRequestDTO) {
    Doctor doctor = doctorRepository.save(modelMapper.map(doctorRequestDTO, Doctor.class));
    DoctorResponseDTO doctorResponseDTO = modelMapper.map(doctor, DoctorResponseDTO.class);
    return doctorResponseDTO;
  }

  @Override
  public DoctorResponseDTO update(DoctorRequestDTO doctorRequestDTO, Long id) {
    Optional<Doctor> optionalDoctor = doctorRepository.findById(id);
    if (optionalDoctor.isPresent()) {
      Doctor doctor = optionalDoctor.get();
      modelMapper.map(doctorRequestDTO, doctor);
      Doctor updatedDoctor = doctorRepository.save(doctor);
      DoctorResponseDTO doctorResponseDTO = modelMapper.map(updatedDoctor, DoctorResponseDTO.class);
      return doctorResponseDTO;
    }
    throw new RuntimeException("Doctor not found");
  }

  @Override
  public void deleteById(Long id) {
    doctorRepository.deleteById(id);
  }

}
