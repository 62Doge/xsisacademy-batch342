package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;

import com._a.backend.dtos.requests.MenuRequestDTO;
import com._a.backend.dtos.responses.MenuResponseDTO;
import com._a.backend.services.Services;

public class MenuServiceImpl implements Services<MenuRequestDTO,MenuResponseDTO>{

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<MenuResponseDTO> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<MenuResponseDTO> findById(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public MenuResponseDTO save(MenuRequestDTO requestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MenuResponseDTO update(MenuRequestDTO requestDTO, Long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
