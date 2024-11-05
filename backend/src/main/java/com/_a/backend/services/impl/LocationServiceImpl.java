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

import com._a.backend.dtos.requests.LocationRequestDTO;
import com._a.backend.dtos.responses.LocationResponseDTO;
import com._a.backend.entities.Location;
import com._a.backend.repositories.LocationRepository;
import com._a.backend.services.Services;

import jakarta.transaction.Transactional;

@Service
public class LocationServiceImpl implements Services<LocationRequestDTO, LocationResponseDTO> {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<LocationResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Location> locations = locationRepository.findAllByIsDeleteFalse(pageable);
        Page<LocationResponseDTO> locationResponseDTOS = locations
                .map(location -> modelMapper.map(location, LocationResponseDTO.class));
        return locationResponseDTOS;
    }

    @Override
    public void deleteById(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public List<LocationResponseDTO> findAll() {
        // using less optimal approach, but easier to understand
        return locationRepository.findAll().stream()
                .map(location -> modelMapper.map(location, LocationResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LocationResponseDTO> findById(Long id) {
        return locationRepository.findById(id)
                .map(location -> modelMapper.map(location, LocationResponseDTO.class));
    }

    @Override
    public LocationResponseDTO save(LocationRequestDTO locationRequestDTO) {
        Location location = locationRepository.save(modelMapper.map(locationRequestDTO, Location.class));
        return modelMapper.map(location, LocationResponseDTO.class);
    }

    @Transactional
    @Override
    public LocationResponseDTO update(LocationRequestDTO locationRequestDTO, Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        modelMapper.map(locationRequestDTO, location);
        Location updatedLocation = locationRepository.save(location);

        return modelMapper.map(updatedLocation, LocationResponseDTO.class);
    }

}
