package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.BiodataRequestDTO;
import com._a.backend.dtos.responses.BiodataResponseDTO;
import com._a.backend.entities.Biodata;
import com._a.backend.repositories.BiodataRepository;
import com._a.backend.services.Services;

import jakarta.transaction.Transactional;

@Service
public class BiodataServiceImpl implements Services<BiodataRequestDTO, BiodataResponseDTO> {

    @Autowired
    BiodataRepository biodataRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public void deleteById(Long id) {
        biodataRepository.deleteById(id);
    }

    @Override
    public List<BiodataResponseDTO> findAll() {
        // using less optimal approach, but easier to understand
        return biodataRepository.findAll().stream()
                .map(biodata -> modelMapper.map(biodata, BiodataResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BiodataResponseDTO> findById(Long id) {
        return biodataRepository.findById(id)
                .map(biodata -> modelMapper.map(biodata, BiodataResponseDTO.class));
    }

    @Override
    public BiodataResponseDTO save(BiodataRequestDTO biodataRequestDTO) {
        Biodata biodata = biodataRepository.save(modelMapper.map(biodataRequestDTO, Biodata.class));
        return modelMapper.map(biodata, BiodataResponseDTO.class);
    }

    @Transactional
    @Override
    public BiodataResponseDTO update(BiodataRequestDTO biodataRequestDTO, Long id) {
        Biodata biodata = biodataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Biodata not found"));

        modelMapper.map(biodataRequestDTO, biodata);
        Biodata updatedBiodata = biodataRepository.save(biodata);

        return modelMapper.map(updatedBiodata, BiodataResponseDTO.class);
    }

}
