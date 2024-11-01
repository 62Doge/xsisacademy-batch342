package com._a.backend.controllers;

import com._a.backend.dtos.requests.LocationLevelRequestDTO;
import com._a.backend.dtos.responses.LocationLevelResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.LocationLevelRepository;
import com._a.backend.services.impl.LocationLevelServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/location-level")
public class LocationLevelController {
    @Autowired
    private LocationLevelServiceImpl locationLevelService;
    @Autowired
    private LocationLevelRepository locationLevelRepository;

    @GetMapping("")
    public ResponseEntity<?> findAllLocationLevels() {
        try {
            List<LocationLevelResponseDTO> locationLevelResponseDTOS = locationLevelService.findAll();

            ApiResponse<List<LocationLevelResponseDTO>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), locationLevelResponseDTOS);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        }catch (Exception e) {
            ApiResponse<List<LocationLevelResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveLocationLevel(@Valid @RequestBody LocationLevelRequestDTO locationLevelRequestDTO) {
        if (locationLevelRepository.existsByName(locationLevelRequestDTO.getName())) {
            ApiResponse<LocationLevelResponseDTO> alreadyExistResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), "Location Level name already exists", null);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(alreadyExistResponse);
        }

        try {
            LocationLevelResponseDTO locationLevelResponseDTOSaved = locationLevelService.save(locationLevelRequestDTO);
            ApiResponse<LocationLevelResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), locationLevelResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<LocationLevelResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save Location Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLocationLevel(@Valid @RequestBody LocationLevelRequestDTO locationLevelRequestDTO, @PathVariable Long id) {
        try {
            Optional<LocationLevelResponseDTO> locationLevelResponseDTO = locationLevelService.findById(id);

            if (locationLevelResponseDTO.isEmpty()) {
                ApiResponse<LocationLevelResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Location Level not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            LocationLevelResponseDTO locationLevelResponseDTOSaved = locationLevelService.update(locationLevelRequestDTO, id);

            ApiResponse<LocationLevelResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), locationLevelResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<LocationLevelResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update Location Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

//    @PatchMapping("/soft-delete/{id}")

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteLocationLevel(@PathVariable Long id) {
        try{
            Optional<LocationLevelResponseDTO> locationLevelResponseDTO = locationLevelService.findById(id);

            if (locationLevelResponseDTO.isEmpty()) {
                ApiResponse<LocationLevelResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Location Level not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            locationLevelService.deleteById(id);
            ApiResponse<LocationLevelResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "Location Level deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<LocationLevelResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete Location Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }


}
