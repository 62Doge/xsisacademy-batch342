package com._a.backend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.LocationLevelRequestDTO;
import com._a.backend.dtos.responses.LocationLevelResponseDTO;
import com._a.backend.entities.LocationLevel;
import com._a.backend.repositories.LocationLevelRepository;
import com._a.backend.services.Services;

@Service
public class LocationLevelServiceImpl implements Services<LocationLevelRequestDTO, LocationLevelResponseDTO> {
    @Autowired
    private LocationLevelRepository locationLevelRepository;

    @Autowired
    private ModelMapper modelMapper;


//    with pagination
   @Override
    public Page<LocationLevelResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<LocationLevel> locationLevels = locationLevelRepository.getLocationLevelWithCreatedOn(pageable);
        Page<LocationLevelResponseDTO> locationLevelResponseDTOS =
                locationLevels.map(locationLevel -> modelMapper.map(locationLevel, LocationLevelResponseDTO.class));
        return locationLevelResponseDTOS;
    }

    @Override
    public  List<LocationLevelResponseDTO> findAll() {
        List<LocationLevel> locationLevels = locationLevelRepository.findAllByIsDeleteFalse();
        List<LocationLevelResponseDTO> locationLevelResponseDTOs = locationLevels.stream().map(
                locationLevel -> modelMapper.map(locationLevel, LocationLevelResponseDTO.class)).toList();
        return locationLevelResponseDTOs;
    }

    public Page<LocationLevelResponseDTO> getByName(int pageNo, int pageSize, String sortBy, String sortDirection, String name) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<LocationLevel> locationLevels = locationLevelRepository.findByNameContainingIgnoreCaseAndIsDeleteFalse(pageable, name);
        Page<LocationLevelResponseDTO> locationLevelResponseDTOS =
                locationLevels.map(locationLevel -> modelMapper.map(locationLevel, LocationLevelResponseDTO.class));
        return locationLevelResponseDTOS;
    }

    @Override
    public Optional<LocationLevelResponseDTO> findById(Long id) {
        Optional<LocationLevel> locationLevel = locationLevelRepository.findById(id);
        if (locationLevel.isPresent()) {
            Optional<LocationLevelResponseDTO> locationLevelResponseDTO = locationLevel.map(
                    locationLevelOptional -> modelMapper.map(locationLevelOptional, LocationLevelResponseDTO.class));
            return locationLevelResponseDTO;
        }

        return Optional.empty();
    }

    @Override
    public LocationLevelResponseDTO save(LocationLevelRequestDTO locationLevelRequestDTO) {
       if (locationLevelRepository.existsByNameContainingIgnoreCaseAndIsDeleteFalse(locationLevelRequestDTO.getName())){
           throw new IllegalArgumentException("Name already exists");
        }

        LocationLevel locationLevel = locationLevelRepository.save(modelMapper.map(locationLevelRequestDTO, LocationLevel.class));
        LocationLevelResponseDTO locationLevelResponseDTO = modelMapper.map(locationLevel, LocationLevelResponseDTO.class);
        return locationLevelResponseDTO;
    }

    @Override
    public LocationLevelResponseDTO update(LocationLevelRequestDTO locationLevelRequestDTO, Long id) {
        Optional<LocationLevel> optionalLocationLevel = locationLevelRepository.findById(id);
        if (locationLevelRepository.existsByNameContainingIgnoreCaseAndIsDeleteFalseAndIdNot(locationLevelRequestDTO.getName(), id)) {
            throw new IllegalArgumentException("Name already exists");

        } else if (optionalLocationLevel.isPresent()) {
            LocationLevel locationLevel = optionalLocationLevel.get();

            modelMapper.map(locationLevelRequestDTO, locationLevel);

            LocationLevel updatedLocationLevel = locationLevelRepository.save(locationLevel);

            return modelMapper.map(updatedLocationLevel, LocationLevelResponseDTO.class);
        }
        throw new IllegalArgumentException("Location Level not found");
    }

    public void softDeleteLocationLevel(Long id) {
        LocationLevel locationLevel = locationLevelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("LocationLevel not found"));

        boolean hasActiveLocations = locationLevel.getLocations().stream()
                .anyMatch(location -> !location.getIsDelete());

        if (hasActiveLocations) {
            throw new IllegalStateException("Cannot delete location level: it's active on locations.");
        }

        locationLevel.setIsDelete(true);
//        locationLevel.setDeletedBy(userId);
        locationLevel.setDeletedOn(LocalDateTime.now());
        locationLevelRepository.save(locationLevel);
    }

    @Override
    public void deleteById(Long id) {
        locationLevelRepository.deleteById(id);
    }
}
