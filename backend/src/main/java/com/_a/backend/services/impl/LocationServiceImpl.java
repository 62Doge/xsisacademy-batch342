package com._a.backend.services.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.LocationRequestDTO;
import com._a.backend.dtos.responses.LocationResponseDTO;
import com._a.backend.entities.Location;
import com._a.backend.repositories.LocationRepository;
import com._a.backend.services.Services;

@Service
public class LocationServiceImpl implements Services<LocationRequestDTO, LocationResponseDTO> {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<LocationResponseDTO> findAll() {
        List<Location> locations = locationRepository.findAll();
        List<LocationResponseDTO> locationResponseDTOs = locations.stream().map(
                location -> modelMapper.map(location, LocationResponseDTO.class)).toList();
        return locationResponseDTOs;
    }

    @Override
    public Optional<LocationResponseDTO> findById(Long id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            Optional<LocationResponseDTO> locationResponseDTO = location.map(
                    locationOptional -> modelMapper.map(locationOptional, LocationResponseDTO.class));
            return locationResponseDTO;
        }

        return Optional.empty();
    }

    @Override
    public LocationResponseDTO save(LocationRequestDTO locationRequestDTO) {
        Location location = locationRepository.save(modelMapper.map(locationRequestDTO, Location.class));
        LocationResponseDTO locationResponseDTO = modelMapper.map(location, LocationResponseDTO.class);
        return locationResponseDTO;
    }

    @Override
    public LocationResponseDTO update(LocationRequestDTO locationRequestDTO, Long id) {
        Optional<Location> optionalLocation = locationRepository.findById(id);
        if (optionalLocation.isPresent()) {
            Location location = optionalLocation.get();

            modelMapper.map(locationRequestDTO, location);

            Location updatedLocation = locationRepository.save(location);

            return modelMapper.map(updatedLocation, LocationResponseDTO.class);
        }
        throw new RuntimeException("Location  not found");
    }

//    public LocationResponseDTO softDelete(Long id) {
//        Location location = locationRepository.findById(id).get();
//    }

    @Override
    public void deleteById(Long id) {
        locationRepository.deleteById(id);
    }
}
