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

import com._a.backend.dtos.requests.MenuRequestDTO;
import com._a.backend.dtos.responses.MenuResponseDTO;
import com._a.backend.entities.Menu;
import com._a.backend.repositories.MenuRepository;
import com._a.backend.services.Services;

import jakarta.transaction.Transactional;

@Service
public class MenuServiceImpl implements Services<MenuRequestDTO, MenuResponseDTO> {

    @Autowired
    MenuRepository menuRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<MenuResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Menu> menus = menuRepository.findAllByIsDeleteFalse(pageable);
        Page<MenuResponseDTO> menuResponseDTOS = menus
                .map(menu -> modelMapper.map(menu, MenuResponseDTO.class));
        return menuResponseDTOS;
    }

    @Override
    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    @Override
    public List<MenuResponseDTO> findAll() {
        // using less optimal approach, but easier to understand
        return menuRepository.findAll().stream()
                .map(menu -> modelMapper.map(menu, MenuResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MenuResponseDTO> findById(Long id) {
        return menuRepository.findById(id)
                .map(menu -> modelMapper.map(menu, MenuResponseDTO.class));
    }

    @Override
    public MenuResponseDTO save(MenuRequestDTO menuRequestDTO) {
        Menu menu = menuRepository.save(modelMapper.map(menuRequestDTO, Menu.class));
        return modelMapper.map(menu, MenuResponseDTO.class);
    }

    @Transactional
    @Override
    public MenuResponseDTO update(MenuRequestDTO menuRequestDTO, Long id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location Level not found"));

        modelMapper.map(menuRequestDTO, menu);
        Menu updatedMenu = menuRepository.save(menu);

        return modelMapper.map(updatedMenu, MenuResponseDTO.class);
    }

    @Transactional
    public List<MenuResponseDTO> getMenuByRoleId(Long roleId) {
        return menuRepository.findMenusByRole(roleId).stream()
                .map(menu -> modelMapper.map(menu, MenuResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MenuResponseDTO> getMenuByUniversalAccess() {
        return menuRepository.findMenusByUniversalAccess().stream()
                .map(menu -> modelMapper.map(menu, MenuResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<MenuResponseDTO> getMenuByRoleOrUniversalAccess(Long roleId) {
        return menuRepository.findMenusByRoleOrUniversalAccess(roleId).stream()
                .map(menu -> modelMapper.map(menu, MenuResponseDTO.class))
                .collect(Collectors.toList());
    }

}
