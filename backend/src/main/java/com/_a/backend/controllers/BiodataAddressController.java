package com._a.backend.controllers;

import java.util.List;
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

import com._a.backend.dtos.requests.BiodataAddressIdsRequestDTO;
import com._a.backend.dtos.requests.BiodataAddressRequestDTO;
import com._a.backend.dtos.responses.BiodataAddressResponseDTO;
import com._a.backend.dtos.responses.PaginatedResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.BiodataAddressServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/biodata-address")
@CrossOrigin("*")
public class BiodataAddressController {

    @Autowired
    private BiodataAddressServiceImpl biodataAddressService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<BiodataAddressResponseDTO>>> getBiodataAddresses(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<BiodataAddressResponseDTO> biodataAddressResponseDTOS = biodataAddressService.getAll(pageNo, pageSize, sortBy,
                    sortDirection);
            PaginatedResponseDTO<BiodataAddressResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(
                    biodataAddressResponseDTOS);

            ApiResponse<PaginatedResponseDTO<BiodataAddressResponseDTO>> successResponse = new ApiResponse<>(
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<BiodataAddressResponseDTO>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/biodata/{biodataId}")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<BiodataAddressResponseDTO>>> getBiodataAddressesByBiodataId(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @PathVariable Long biodataId) {
        try {
            Page<BiodataAddressResponseDTO> biodataAddressResponseDTOS = biodataAddressService.getAllByBiodataId(pageNo, pageSize, sortBy,
                    sortDirection, biodataId);
            PaginatedResponseDTO<BiodataAddressResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(
                    biodataAddressResponseDTOS);

            ApiResponse<PaginatedResponseDTO<BiodataAddressResponseDTO>> successResponse = new ApiResponse<>(
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<BiodataAddressResponseDTO>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBiodataAddressById(@PathVariable("id") Long id) {
        try {
            Optional<BiodataAddressResponseDTO> biodataAddress = biodataAddressService.findById(id);

            if (biodataAddress.isPresent()) {
                ApiResponse<BiodataAddressResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                        HttpStatus.OK.getReasonPhrase(), biodataAddress.get());
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                ApiResponse<BiodataAddressResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                        "BiodataAddress  not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        } catch (Exception e) {
            ApiResponse<BiodataAddressResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/biodata/{biodataId}/search")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<BiodataAddressResponseDTO>>> getBiodataAddressByBiodataIdAndSearchKeyword(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @PathVariable Long biodataId,
            @RequestParam(defaultValue = "") String keyword) {
        try {
            Page<BiodataAddressResponseDTO> biodataAddressResponseDTOS = biodataAddressService.searchByBiodataIdAndKeyword(pageNo, pageSize, sortBy,
                    sortDirection, biodataId, keyword);

            PaginatedResponseDTO<BiodataAddressResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(
                    biodataAddressResponseDTOS);

            ApiResponse<PaginatedResponseDTO<BiodataAddressResponseDTO>> successResponse = new ApiResponse<>(
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<BiodataAddressResponseDTO>> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/multiple-ids")
    public ResponseEntity<?> getLabelByMultipleIds(@RequestBody BiodataAddressIdsRequestDTO biodataAddressIdsRequestDTO) {
        try {
            ApiResponse<List<String>> successResponse = new ApiResponse<>(
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), biodataAddressService.findAllLabelsByBiodataIdInAndIsDeleteFalse(biodataAddressIdsRequestDTO.getIds()));
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveBiodataAddress(@Valid @RequestBody BiodataAddressRequestDTO biodataAddressRequestDTO) {
        try {
            BiodataAddressResponseDTO biodataAddressResponseDTOSaved = biodataAddressService.save(biodataAddressRequestDTO);
            ApiResponse<BiodataAddressResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), biodataAddressResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<BiodataAddressResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to save BiodataAddress ", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBiodataAddress(@Valid @RequestBody BiodataAddressRequestDTO biodataAddressRequestDTO,
            @PathVariable Long id) {
        try {
            BiodataAddressResponseDTO biodataAddressResponseDTOSaved = biodataAddressService.update(biodataAddressRequestDTO, id);
            ApiResponse<BiodataAddressResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), biodataAddressResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<BiodataAddressResponseDTO> badRequestResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (Exception e) {
            ApiResponse<BiodataAddressResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to update BiodataAddress ", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteBiodataAddress(@PathVariable Long id, @RequestBody BiodataAddressRequestDTO BiodataAddressRequestDTO) {
        try {
            biodataAddressService.softDeleteBiodataAddress(id, BiodataAddressRequestDTO.getDeletedBy());
            ApiResponse<BiodataAddressResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    "BiodataAddress  soft deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<BiodataAddressResponseDTO> badRequestResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (Exception e) {
            ApiResponse<BiodataAddressResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to soft delete BiodataAddress ", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PatchMapping("/soft-delete/multiple")
    public ResponseEntity<?> softDeleteBiodataAddresses(@RequestBody BiodataAddressIdsRequestDTO BiodataAddressIdsRequestDTO) {
        try {
            biodataAddressService.softDeleteBiodataAddresses(BiodataAddressIdsRequestDTO.getIds(), BiodataAddressIdsRequestDTO.getDeletedBy());
            ApiResponse<BiodataAddressResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    "BiodataAddresses soft deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<BiodataAddressResponseDTO> badRequestResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (Exception e) {
            ApiResponse<BiodataAddressResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to soft delete BiodataAddresses ", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteBiodataAddress(@PathVariable Long id) {
        try {
            Optional<BiodataAddressResponseDTO> biodataAddressResponseDTO = biodataAddressService.findById(id);

            if (biodataAddressResponseDTO.isEmpty()) {
                ApiResponse<BiodataAddressResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                        "BiodataAddress  not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            biodataAddressService.deleteById(id);
            ApiResponse<BiodataAddressResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    "BiodataAddress  deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<BiodataAddressResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to delete BiodataAddress ", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

}
