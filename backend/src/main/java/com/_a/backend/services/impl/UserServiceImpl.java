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

import com._a.backend.dtos.requests.UserRequestDTO;
import com._a.backend.dtos.responses.UserResponseDTO;
import com._a.backend.entities.User;
import com._a.backend.repositories.UserRepository;
import com._a.backend.services.Services;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements Services<UserRequestDTO, UserResponseDTO> {

    @Autowired
    UserRepository userRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<UserResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<User> users = userRepository.findAllByIsDeleteFalse(pageable);
        Page<UserResponseDTO> userResponseDTOS = users
                .map(user -> modelMapper.map(user, UserResponseDTO.class));
        return userResponseDTOS;
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        // using less optimal approach, but easier to understand
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponseDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserResponseDTO.class));
    }

    @Override
    public UserResponseDTO save(UserRequestDTO userRequestDTO) {
        User user = userRepository.save(modelMapper.map(userRequestDTO, User.class));
        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Transactional
    @Override
    public UserResponseDTO update(UserRequestDTO userRequestDTO, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location Level not found"));

        modelMapper.map(userRequestDTO, user);
        User updatedUser = userRepository.save(user);

        return modelMapper.map(updatedUser, UserResponseDTO.class);
    }

}
