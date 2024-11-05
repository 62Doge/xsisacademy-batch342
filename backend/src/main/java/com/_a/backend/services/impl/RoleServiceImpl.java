package com._a.backend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Page<RoleResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Role> roles = roleRepository.findAllByIsDeleteFalse(pageable);
        Page<RoleResponseDTO> roleResponseDTOS = roles
                .map(role -> modelMapper.map(role, RoleResponseDTO.class));
        return roleResponseDTOS;
    }

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
                .orElseThrow(() -> new RuntimeException("Role not found"));

        modelMapper.map(roleRequestDTO, role);
        Role updatedRole = roleRepository.save(role);

        return modelMapper.map(updatedRole, RoleResponseDTO.class);
    }

    public Page<RoleResponseDTO> getByName(int pageNo, int pageSize, String sortBy, String sortDirection,
            String name) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Role> roles = roleRepository
                .findByNameContainingIgnoreCaseAndIsDeleteFalse(pageable, name);
        Page<RoleResponseDTO> roleResponseDTOS = roles
                .map(role -> modelMapper.map(role, RoleResponseDTO.class));
        return roleResponseDTOS;
    }

    public void softDeleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        boolean hasActiveUsers = role.getUsers().stream()
                .anyMatch(location -> !location.getIsDelete());

        boolean hasActiveMenuRoles = role.getMenuRoles().stream()
                .anyMatch(location -> !location.getIsDelete());

        if (hasActiveUsers || hasActiveMenuRoles) {
            throw new IllegalStateException("Cannot delete role: active role found.");
        }

        role.setIsDelete(true);
        // role.setDeletedBy(userId);
        role.setDeletedOn(LocalDateTime.now());
        roleRepository.save(role);
    }

}
