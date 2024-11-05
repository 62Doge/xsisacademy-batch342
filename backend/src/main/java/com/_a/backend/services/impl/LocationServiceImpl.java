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

    public Page<LocationResponseDTO> findAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        // using less optimal approach, but easier to understand
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Location> locations = locationRepository.findAll(pageable);
        Page<LocationResponseDTO> locationResponseDTOS = locations
                .map(location -> modelMapper.map(location, LocationResponseDTO.class));
        return locationResponseDTOS;
    }

    @Override
    public Optional<LocationResponseDTO> findById(Long id) {
        return locationRepository.findById(id)
                .map(location -> modelMapper.map(location, LocationResponseDTO.class));
    }

    public Page<LocationResponseDTO> getByName(int pageNo, int pageSize, String sortBy, String sortDirection,
            String name) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Location> locations = locationRepository.findByNameContainingIgnoreCaseAndIsDeleteFalse(pageable, name);
        Page<LocationResponseDTO> locationResponseDTOS = locations
                .map(location -> modelMapper.map(location, LocationResponseDTO.class));
        return locationResponseDTOS;
    }

    public Page<LocationResponseDTO> getByLocationLevel(int pageNo, int pageSize, String sortBy, String sortDirection, Long locationLevelId) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();
        
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Location> locations = locationRepository.findByLocationLevelIdAndIsDeleteFalse(pageable, locationLevelId);
        Page<LocationResponseDTO> locationResponseDTOS = locations
                .map(location -> modelMapper.map(location, LocationResponseDTO.class));
        return locationResponseDTOS;
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

        boolean hasActiveLocations = location.getChilds().stream()
                .anyMatch(childLocation -> !childLocation.getIsDelete());

        if (!location.getLocationLevelId().equals(locationRequestDTO.getLocationLevelId()) && hasActiveLocations) {
            throw new IllegalStateException("Cannot edit location's level: Active usage found.");
        }

        modelMapper.map(locationRequestDTO, location);
        Location updatedLocation = locationRepository.save(location);

        return modelMapper.map(updatedLocation, LocationResponseDTO.class);
    }

    public void softDeleteLocation(Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));

        boolean hasActiveLocations = location.getChilds().stream()
                .anyMatch(childLocation -> !childLocation.getIsDelete());

        boolean hasActiveBiodataAddress = location.getBiodataAddresses().stream()
                .anyMatch(biodataAddress -> !biodataAddress.getIsDelete());

        boolean hasActiveMedicalFacility = location.getMedicalFacilities().stream()
                .anyMatch(medicalFacility -> !medicalFacility.getIsDelete());
        // m_biodata_address & m_medical_facility uses this (and also itself)
        if (hasActiveLocations || hasActiveBiodataAddress || hasActiveMedicalFacility) {
            throw new IllegalStateException("Cannot delete location : Active usage found.");
        }

        location.setIsDelete(true);
        // location.setDeletedBy(userId);
        location.setDeletedOn(LocalDateTime.now());
        locationRepository.save(location);
    }

}
