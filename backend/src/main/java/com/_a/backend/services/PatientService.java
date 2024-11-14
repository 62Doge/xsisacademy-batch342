package com._a.backend.services;

import com._a.backend.dtos.responses.PatientChatNumberResponseDTO;

public interface PatientService<PatientRequestDTO, PatientResponseDTO> {
    
    PatientResponseDTO findById(Long id);

    // Optional<PatientResponseDTO> findById(Long id);

    PatientResponseDTO save(Long parentBiodataId, PatientRequestDTO patientRequestDTO);

    PatientResponseDTO update(Long id, Long parentBiodataId, PatientRequestDTO patientRequestDTO);

    PatientChatNumberResponseDTO getChatNumber(Long customerMemberId);

}
