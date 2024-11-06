package com._a.backend.services;

import java.util.List;

public interface PatientService<PatientRequestDTO, PatientResponseDTO> {
    
    List<PatientResponseDTO> findAll();

    PatientResponseDTO findById(Long id);

    // Optional<PatientResponseDTO> findById(Long id);

    PatientResponseDTO save(Long parentBiodataId, PatientRequestDTO patientRequestDTO);

    PatientResponseDTO update(Long id, Long parentBiodataId, PatientRequestDTO patientRequestDTO);

}
