package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;

import com._a.backend.dtos.requests.UserRequestDTO;
import com._a.backend.dtos.responses.UserResponseDTO;
import com._a.backend.services.Services;

public class UserServiceImpl implements Services<UserRequestDTO,UserResponseDTO>{

    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<UserResponseDTO> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Optional<UserResponseDTO> findById(Long id) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public UserResponseDTO save(UserRequestDTO requestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserResponseDTO update(UserRequestDTO requestDTO, Long id) {
        // TODO Auto-generated method stub
        return null;
    }

}
