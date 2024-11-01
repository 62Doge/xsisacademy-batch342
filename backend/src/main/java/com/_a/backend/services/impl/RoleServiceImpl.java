package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;

import com._a.backend.dtos.requests.RoleRequestDTO;
import com._a.backend.dtos.responses.RoleResponseDTO;
import com._a.backend.services.Services;

public class RoleServiceImpl implements Services<RoleRequestDTO,RoleResponseDTO>{

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<RoleResponseDTO> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<RoleResponseDTO> findById(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public RoleResponseDTO save(RoleRequestDTO requestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RoleResponseDTO update(RoleRequestDTO requestDTO, Long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
