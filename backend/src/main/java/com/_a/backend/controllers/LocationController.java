package com._a.backend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.LocationRequestDTO;
import com._a.backend.dtos.responses.LocationResponseDTO;
import com._a.backend.dtos.responses.PaginatedResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.LocationRepository;
import com._a.backend.services.impl.LocationServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/location")
@CrossOrigin("*")
public class LocationController {
    @Autowired
    private LocationServiceImpl locationService;
    @Autowired
    private LocationRepository locationRepository;

    @GetMapping("")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<LocationResponseDTO>>> getLocations(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<LocationResponseDTO> locationResponseDTOS = locationService.getAll(pageNo, pageSize, sortBy,
                    sortDirection);
            PaginatedResponseDTO<LocationResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(
                    locationResponseDTOS);

            ApiResponse<PaginatedResponseDTO<LocationResponseDTO>> successResponse = new ApiResponse<>(
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<LocationResponseDTO>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    // using paging above
    // @GetMapping("")
    // public ResponseEntity<?> findAllLocations() {
    // try {
    // List<LocationResponseDTO> locationResponseDTOS = locationService.findAll();
    //
    // ApiResponse<List<LocationResponseDTO>> successResponse =
    // new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
    // locationResponseDTOS);
    // return new ResponseEntity<>(successResponse, HttpStatus.OK);
    // }catch (Exception e) {
    // ApiResponse<List<LocationResponseDTO>> errorResponse =
    // new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
    // HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
    // return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    // }
    // }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable("id") Long id) {
        try {
            Optional<LocationResponseDTO> location = locationService.findById(id);

            if (location.isPresent()) {
                ApiResponse<LocationResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(), location.get());
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                ApiResponse<LocationResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                        "Location  not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        } catch (Exception e) {
            ApiResponse<LocationResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<LocationResponseDTO>>> getLocationByName(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @PathVariable String name) {
        try {
            Page<LocationResponseDTO> locationResponseDTOS = locationService.getByName(pageNo, pageSize, sortBy,
                    sortDirection, name);

            PaginatedResponseDTO<LocationResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(
                    locationResponseDTOS);

            ApiResponse<PaginatedResponseDTO<LocationResponseDTO>> successResponse = new ApiResponse<>(
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<LocationResponseDTO>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/level/{locationLevelId}")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<LocationResponseDTO>>> getLocationByLocationLevel(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @PathVariable Long locationLevelId) {
        try {
            Page<LocationResponseDTO> locationResponseDTOS = locationService.getByLocationLevel(pageNo, pageSize,
                    sortBy, sortDirection, locationLevelId);

            PaginatedResponseDTO<LocationResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(
                    locationResponseDTOS);

            ApiResponse<PaginatedResponseDTO<LocationResponseDTO>> successResponse = new ApiResponse<>(
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<LocationResponseDTO>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveLocation(@Valid @RequestBody LocationRequestDTO locationRequestDTO) {
        if (locationRepository.existsByNameAndParentId(locationRequestDTO.getName(),
                locationRequestDTO.getParentId())) {
            ApiResponse<LocationResponseDTO> alreadyExistResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(),
                    "Location name already exists", null);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(alreadyExistResponse);
        }

        try {
            LocationResponseDTO locationResponseDTOSaved = locationService.save(locationRequestDTO);
            ApiResponse<LocationResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), locationResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<LocationResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to save Location ", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLocation(@Valid @RequestBody LocationRequestDTO locationRequestDTO,
            @PathVariable Long id) {
        if (locationRepository.existsByNameAndParentId(locationRequestDTO.getName(),
                locationRequestDTO.getParentId())) {
            ApiResponse<LocationResponseDTO> alreadyExistResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(),
                    "Location name already exists", null);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(alreadyExistResponse);
        }
        try {
            LocationResponseDTO locationResponseDTOSaved = locationService.update(locationRequestDTO, id);
            ApiResponse<LocationResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), locationResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<LocationResponseDTO> badRequestResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (Exception e) {
            ApiResponse<LocationResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to update Location ", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteLocation(@PathVariable Long id) {
        try {
            locationService.softDeleteLocation(id);

            ApiResponse<LocationResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    "Location  soft deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<LocationResponseDTO> badRequestResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (IllegalStateException e) {
            ApiResponse<LocationResponseDTO> conflictResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(),
                    e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflictResponse);
        } catch (Exception e) {
            ApiResponse<LocationResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to soft delete Location ", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteLocation(@PathVariable Long id) {
        try {
            Optional<LocationResponseDTO> locationResponseDTO = locationService.findById(id);

            if (locationResponseDTO.isEmpty()) {
                ApiResponse<LocationResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                        "Location  not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            locationService.deleteById(id);
            ApiResponse<LocationResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    "Location  deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<LocationResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to delete Location ", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

}
