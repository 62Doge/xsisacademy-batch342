package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.RoleRequestDTO;
import com._a.backend.dtos.responses.RoleResponseDTO;
import com._a.backend.entities.Role;
import com._a.backend.repositories.RoleRepository;
import com._a.backend.services.Services;

import jakarta.transaction.Transactional;

@Service
public class RoleServiceImpl implements Services<RoleRequestDTO, RoleResponseDTO> {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public List<RoleResponseDTO> findAll() {
        // using less optimal approach, but easier to understand
        return roleRepository.findAll().stream()
                .map(role -> modelMapper.map(role, RoleResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleResponseDTO> findById(Long id) {
        return roleRepository.findById(id)
                .map(role -> modelMapper.map(role, RoleResponseDTO.class));
    }

    @Override
    public RoleResponseDTO save(RoleRequestDTO roleRequestDTO) {
        Role role = roleRepository.save(modelMapper.map(roleRequestDTO, Role.class));
        return modelMapper.map(role, RoleResponseDTO.class);
    }

    @Transactional
    @Override
    public RoleResponseDTO update(RoleRequestDTO roleRequestDTO, Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location Level not found"));

        modelMapper.map(roleRequestDTO, role);
        Role updatedRole = roleRepository.save(role);

        return modelMapper.map(updatedRole, RoleResponseDTO.class);
    }

}
