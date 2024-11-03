package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

}
