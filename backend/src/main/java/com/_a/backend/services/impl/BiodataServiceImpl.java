package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;

import com._a.backend.dtos.requests.BiodataRequestDTO;
import com._a.backend.dtos.responses.BiodataResponseDTO;
import com._a.backend.services.Services;

public class BiodataServiceImpl implements Services<BiodataRequestDTO,BiodataResponseDTO>{

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<BiodataResponseDTO> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<BiodataResponseDTO> findById(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public BiodataResponseDTO save(BiodataRequestDTO requestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BiodataResponseDTO update(BiodataRequestDTO requestDTO, Long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
