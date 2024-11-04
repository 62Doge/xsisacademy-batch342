package com._a.backend.controllers;

import com._a.backend.dtos.requests.LocationLevelRequestDTO;
import com._a.backend.dtos.responses.LocationLevelResponseDTO;
import com._a.backend.dtos.responses.PaginatedResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.LocationLevelRepository;
import com._a.backend.services.impl.LocationLevelServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/location-level")
@CrossOrigin("*")
public class LocationLevelController {
    @Autowired
    private LocationLevelServiceImpl locationLevelService;
    @Autowired
    private LocationLevelRepository locationLevelRepository;


    @GetMapping("")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<LocationLevelResponseDTO>>> getLocationLevels(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<LocationLevelResponseDTO> locationLevelResponseDTOS =
                        locationLevelService.getAll(pageNo, pageSize, sortBy, sortDirection);
            PaginatedResponseDTO<LocationLevelResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(locationLevelResponseDTOS);

            ApiResponse<PaginatedResponseDTO<LocationLevelResponseDTO>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }catch (Exception e){
            ApiResponse<PaginatedResponseDTO<LocationLevelResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
//    using paging above
//    @GetMapping("")
//    public ResponseEntity<?> findAllLocationLevels() {
//        try {
//            List<LocationLevelResponseDTO> locationLevelResponseDTOS = locationLevelService.findAll();
//
//            ApiResponse<List<LocationLevelResponseDTO>> successResponse =
//                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), locationLevelResponseDTOS);
//            return new ResponseEntity<>(successResponse, HttpStatus.OK);
//        }catch (Exception e) {
//            ApiResponse<List<LocationLevelResponseDTO>> errorResponse =
//                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
//            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationLevelById(@PathVariable("id") Long id) {
        try {
            Optional<LocationLevelResponseDTO> locationLevel = locationLevelService.findById(id);

            if (locationLevel.isPresent()) {
                ApiResponse<LocationLevelResponseDTO> successResponse =
                        new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), locationLevel.get());
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                ApiResponse<LocationLevelResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Location Level not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        }catch (Exception e) {
            ApiResponse<LocationLevelResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<LocationLevelResponseDTO>>> getLocationLevelByName(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @PathVariable String name) {
        try {
            Page<LocationLevelResponseDTO> locationLevelResponseDTOS =
                    locationLevelService.getByName(pageNo, pageSize, sortBy, sortDirection, name);

            PaginatedResponseDTO<LocationLevelResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(locationLevelResponseDTOS);

            ApiResponse<PaginatedResponseDTO<LocationLevelResponseDTO>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<LocationLevelResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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
            LocationLevelResponseDTO locationLevelResponseDTOSaved = locationLevelService.update(locationLevelRequestDTO, id);
            ApiResponse<LocationLevelResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), locationLevelResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<LocationLevelResponseDTO> badRequestResponse =
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (Exception e) {
            ApiResponse<LocationLevelResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update Location Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteLocationLevel(@PathVariable Long id) {
        try {
            locationLevelService.softDeleteLocationLevel(id);

            ApiResponse<LocationLevelResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), "Location Level soft deleted", null);
            return ResponseEntity.ok(successResponse);
        }catch (IllegalArgumentException e) {
            ApiResponse<LocationLevelResponseDTO> badRequestResponse =
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (IllegalStateException e){
            ApiResponse<LocationLevelResponseDTO> conflictResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflictResponse);
        } catch (Exception e) {
            ApiResponse<LocationLevelResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to soft delete Location Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

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
