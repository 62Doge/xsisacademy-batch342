package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;

import com._a.backend.dtos.requests.MenuRoleRequestDTO;
import com._a.backend.dtos.responses.MenuRoleResponseDTO;
import com._a.backend.services.Services;

public class MenuRoleServiceImpl implements Services<MenuRoleRequestDTO,MenuRoleResponseDTO>{

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<MenuRoleResponseDTO> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<MenuRoleResponseDTO> findById(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public MenuRoleResponseDTO save(MenuRoleRequestDTO requestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MenuRoleResponseDTO update(MenuRoleRequestDTO requestDTO, Long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
