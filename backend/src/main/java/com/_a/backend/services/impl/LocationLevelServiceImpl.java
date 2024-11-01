package com._a.backend.services.impl;

import com._a.backend.dtos.requests.LocationLevelRequestDTO;
import com._a.backend.dtos.responses.LocationLevelResponseDTO;
import com._a.backend.entities.LocationLevel;
import com._a.backend.repositories.LocationLevelRepository;
import com._a.backend.services.Services;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationLevelServiceImpl implements Services<LocationLevelRequestDTO, LocationLevelResponseDTO> {
    @Autowired
    private LocationLevelRepository locationLevelRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<LocationLevelResponseDTO> findAll() {
        List<LocationLevel> locationLevels = locationLevelRepository.findAll();
        List<LocationLevelResponseDTO> locationLevelResponseDTOs = locationLevels.stream().map(
                locationLevel -> modelMapper.map(locationLevel, LocationLevelResponseDTO.class)).toList();
        return locationLevelResponseDTOs;
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
        LocationLevel locationLevel = locationLevelRepository.save(modelMapper.map(locationLevelRequestDTO, LocationLevel.class));
        LocationLevelResponseDTO locationLevelResponseDTO = modelMapper.map(locationLevel, LocationLevelResponseDTO.class);
        return locationLevelResponseDTO;
    }

    @Override
    public LocationLevelResponseDTO update(LocationLevelRequestDTO locationLevelRequestDTO, Long id) {
        Optional<LocationLevel> optionalLocationLevel = locationLevelRepository.findById(id);
        if (optionalLocationLevel.isPresent()) {
            LocationLevel locationLevel = optionalLocationLevel.get();

            modelMapper.map(locationLevelRequestDTO, locationLevel);

            LocationLevel updatedLocationLevel = locationLevelRepository.save(locationLevel);

            return modelMapper.map(updatedLocationLevel, LocationLevelResponseDTO.class);
        }
        throw new RuntimeException("Location Level not found");
    }

//    public LocationLevelResponseDTO softDelete(Long id) {
//        LocationLevel locationLevel = locationLevelRepository.findById(id).get();
//    }

    @Override
    public void deleteById(Long id) {
        locationLevelRepository.deleteById(id);
    }
}
