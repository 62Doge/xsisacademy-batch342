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

import com._a.backend.dtos.requests.MedicalItemRequestDTO;
import com._a.backend.dtos.responses.MedicalItemResponseDTO;
import com._a.backend.dtos.responses.PaginatedResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.MedicalItemServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/medical-item")
@CrossOrigin("*")
public class MedicalItemController {

    @Autowired
    private MedicalItemServiceImpl medicalItemService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<MedicalItemResponseDTO>>> getMedicalItems(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<MedicalItemResponseDTO> medicalItemResponseDTOS
                    = medicalItemService.getAll(pageNo, pageSize, sortBy, sortDirection);
            PaginatedResponseDTO<MedicalItemResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(medicalItemResponseDTOS);

            ApiResponse<PaginatedResponseDTO<MedicalItemResponseDTO>> successResponse
                    = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<MedicalItemResponseDTO>> errorResponse
                    = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicalItemById(@PathVariable("id") Long id) {
        try {
            Optional<MedicalItemResponseDTO> medicalItem = medicalItemService.findById(id);

            if (medicalItem.isPresent()) {
                ApiResponse<MedicalItemResponseDTO> successResponse
                        = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), medicalItem.get());
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                ApiResponse<MedicalItemResponseDTO> notFoundResponse
                        = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Medical Item Level not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        } catch (Exception e) {
            ApiResponse<MedicalItemResponseDTO> errorResponse
                    = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveMedicalItem(@Valid @RequestBody MedicalItemRequestDTO medicalItemRequestDTO) {
        try {
            MedicalItemResponseDTO medicalItemResponseDTOSaved = medicalItemService.save(medicalItemRequestDTO);

            ApiResponse<MedicalItemResponseDTO> successResponse
                    = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), medicalItemResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<MedicalItemResponseDTO> alreadyExistResponse
                    = new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(alreadyExistResponse);
        } catch (Exception e) {
            ApiResponse<MedicalItemResponseDTO> errorResponse
                    = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save Medical Item Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMedicalItem(@Valid @RequestBody MedicalItemRequestDTO medicalItemRequestDTO, @PathVariable Long id) {
        try {
            MedicalItemResponseDTO medicalItemResponseDTOSaved = medicalItemService.update(medicalItemRequestDTO, id);
            ApiResponse<MedicalItemResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), medicalItemResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<MedicalItemResponseDTO> badRequestResponse
                    = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (Exception e) {
            ApiResponse<MedicalItemResponseDTO> errorResponse
                    = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update Medical Item Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteMedicalItem(@PathVariable Long id, @RequestBody MedicalItemRequestDTO medicalItemRequestDTO) {
        try {
            medicalItemService.softDeleteMedicalItem(id, medicalItemRequestDTO.getDeletedBy());

            ApiResponse<MedicalItemResponseDTO> successResponse
                    = new ApiResponse<>(HttpStatus.OK.value(), "Medical Item Level soft deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<MedicalItemResponseDTO> badRequestResponse
                    = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (IllegalStateException e) {
            ApiResponse<MedicalItemResponseDTO> conflictResponse
                    = new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflictResponse);
        } catch (Exception e) {
            ApiResponse<MedicalItemResponseDTO> errorResponse
                    = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to soft delete Medical Item Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteMedicalItem(@PathVariable Long id) {
        try {
            Optional<MedicalItemResponseDTO> medicalItemResponseDTO = medicalItemService.findById(id);

            if (medicalItemResponseDTO.isEmpty()) {
                ApiResponse<MedicalItemResponseDTO> notFoundResponse
                        = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Medical Item Level not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            medicalItemService.deleteById(id);
            ApiResponse<MedicalItemResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "Medical Item Level deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<MedicalItemResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete Medical Item Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<MedicalItemResponseDTO>>> searchMedicalItems(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "8") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) Long medicalItemCategoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String segmentationName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        try {
            Page<MedicalItemResponseDTO> medicalItemResponseDTOS
                    = medicalItemService.searchMedicalItems(pageNo, pageSize, sortBy, sortDirection, medicalItemCategoryId, keyword, segmentationName, minPrice, maxPrice);
            PaginatedResponseDTO<MedicalItemResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(medicalItemResponseDTOS);

            ApiResponse<PaginatedResponseDTO<MedicalItemResponseDTO>> successResponse
                    = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<MedicalItemResponseDTO>> errorResponse
                    = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/search/v2")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<MedicalItemResponseDTO>>> searchMedicalItemsVersionTwo(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "8") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String segmentationName,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice
    ) {
        try {
            Page<MedicalItemResponseDTO> medicalItemResponseDTOS
                    = medicalItemService.searchMedicalItemsVersionTwo(pageNo, pageSize, sortBy, sortDirection, categoryName, keyword, segmentationName, minPrice, maxPrice);
            PaginatedResponseDTO<MedicalItemResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(medicalItemResponseDTOS);

            ApiResponse<PaginatedResponseDTO<MedicalItemResponseDTO>> successResponse
                    = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<MedicalItemResponseDTO>> errorResponse
                    = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
