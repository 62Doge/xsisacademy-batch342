package com._a.backend.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @Transactional
    public void updateMenuAccessForRole(Long roleId, List<Long> selectedMenuIds) {
        // Step 1: Retrieve existing MenuRole records for this roleId
        List<MenuRole> existingMenuRoles = menuRoleRepository.findByRoleId(roleId);

        // Step 2: Separate the existing menu IDs into two categories: selected and
        // unselected
        Set<Long> selectedMenuIdSet = new HashSet<>(selectedMenuIds);
        Set<Long> existingMenuIds = existingMenuRoles.stream()
                .map(MenuRole::getMenuId)
                .collect(Collectors.toSet());

        // Step 3: Iterate over existing records and update `isDelete` based on
        // selection
        for (MenuRole menuRole : existingMenuRoles) {
            if (selectedMenuIdSet.contains(menuRole.getMenuId())) {
                menuRole.setIsDelete(false); // Grant access
            } else {
                menuRole.setIsDelete(true); // Revoke access
            }
            menuRoleRepository.save(menuRole);
        }

        // Step 4: Insert new MenuRole records for selected menus that don’t exist in
        // current records
        for (Long menuId : selectedMenuIds) {
            if (!existingMenuIds.contains(menuId)) {
                MenuRole newMenuRole = new MenuRole();
                newMenuRole.setRoleId(roleId);
                newMenuRole.setMenuId(menuId);
                newMenuRole.setIsDelete(false);
                menuRoleRepository.save(newMenuRole);
            }
        }
    }

}
