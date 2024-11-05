package com._a.backend.services.impl;

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

import com._a.backend.dtos.requests.MenuRoleRequestDTO;
import com._a.backend.dtos.responses.MenuRoleResponseDTO;
import com._a.backend.entities.MenuRole;
import com._a.backend.repositories.MenuRoleRepository;
import com._a.backend.services.Services;

import jakarta.transaction.Transactional;

@Service
public class MenuRoleServiceImpl implements Services<MenuRoleRequestDTO, MenuRoleResponseDTO> {

    @Autowired
    MenuRoleRepository menuRoleRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<MenuRoleResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<MenuRole> menuRoles = menuRoleRepository.findAllByIsDeleteFalse(pageable);
        Page<MenuRoleResponseDTO> menuRoleResponseDTOS = menuRoles
                .map(menuRole -> modelMapper.map(menuRole, MenuRoleResponseDTO.class));
        return menuRoleResponseDTOS;
    }

    @Override
    public void deleteById(Long id) {
        menuRoleRepository.deleteById(id);
    }

    @Override
    public List<MenuRoleResponseDTO> findAll() {
        // using less optimal approach, but easier to understand
        return menuRoleRepository.findAll().stream()
                .map(menuRole -> modelMapper.map(menuRole, MenuRoleResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MenuRoleResponseDTO> findById(Long id) {
        return menuRoleRepository.findById(id)
                .map(menuRole -> modelMapper.map(menuRole, MenuRoleResponseDTO.class));
    }

    @Override
    public MenuRoleResponseDTO save(MenuRoleRequestDTO menuRoleRequestDTO) {
        MenuRole menuRole = menuRoleRepository.save(modelMapper.map(menuRoleRequestDTO, MenuRole.class));
        return modelMapper.map(menuRole, MenuRoleResponseDTO.class);
    }

    @Transactional
    @Override
    public MenuRoleResponseDTO update(MenuRoleRequestDTO menuRoleRequestDTO, Long id) {
        MenuRole menuRole = menuRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location Level not found"));

        modelMapper.map(menuRoleRequestDTO, menuRole);
        MenuRole updatedMenuRole = menuRoleRepository.save(menuRole);

        return modelMapper.map(updatedMenuRole, MenuRoleResponseDTO.class);
    }

}
