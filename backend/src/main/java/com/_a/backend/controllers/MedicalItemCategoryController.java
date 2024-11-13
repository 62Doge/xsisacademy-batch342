package com._a.backend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.MedicalItemCategoryRequestDTO;
import com._a.backend.dtos.responses.MedicalItemCategoryResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.MedicalItemCategoryServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/medical-item-category")
@CrossOrigin("*")
public class MedicalItemCategoryController {
    @Autowired
    private MedicalItemCategoryServiceImpl medicalItemCategoryService;

    @GetMapping("")
    public ResponseEntity<?> getMedicalItemCategories() {
        try {
            ApiResponse<?> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),medicalItemCategoryService.findAll());
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<?> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    // public ResponseEntity<ApiResponse<PaginatedResponseDTO<MedicalItemCategoryResponseDTO>>> getMedicalItemCategorys(
    //         @RequestParam(defaultValue = "0") int pageNo,
    //         @RequestParam(defaultValue = "3") int pageSize,
    //         @RequestParam(defaultValue = "id") String sortBy,
    //         @RequestParam(defaultValue = "asc") String sortDirection) {
    //     try {
    //         Page<MedicalItemCategoryResponseDTO> medicalItemCategoryResponseDTOS =
    //                     medicalItemCategoryService.getAll(pageNo, pageSize, sortBy, sortDirection);
    //         PaginatedResponseDTO<MedicalItemCategoryResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(medicalItemCategoryResponseDTOS);

    //         ApiResponse<PaginatedResponseDTO<MedicalItemCategoryResponseDTO>> successResponse =
    //                 new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
    //         return ResponseEntity.status(HttpStatus.OK).body(successResponse);
    //     }catch (Exception e){
    //         ApiResponse<PaginatedResponseDTO<MedicalItemCategoryResponseDTO>> errorResponse =
    //                 new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    //     }
    // }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicalItemCategoryById(@PathVariable("id") Long id) {
        try {
            Optional<MedicalItemCategoryResponseDTO> medicalItemCategory = medicalItemCategoryService.findById(id);

            if (medicalItemCategory.isPresent()) {
                ApiResponse<MedicalItemCategoryResponseDTO> successResponse =
                        new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), medicalItemCategory.get());
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                ApiResponse<MedicalItemCategoryResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Medical Item Level not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        }catch (Exception e) {
            ApiResponse<MedicalItemCategoryResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveMedicalItemCategory(@Valid @RequestBody MedicalItemCategoryRequestDTO medicalItemCategoryRequestDTO) {
        try {
            MedicalItemCategoryResponseDTO medicalItemCategoryResponseDTOSaved = medicalItemCategoryService.save(medicalItemCategoryRequestDTO);

            ApiResponse<MedicalItemCategoryResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), medicalItemCategoryResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<MedicalItemCategoryResponseDTO> alreadyExistResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(alreadyExistResponse);
        } catch (Exception e) {
            ApiResponse<MedicalItemCategoryResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save Medical Item Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMedicalItemCategory(@Valid @RequestBody MedicalItemCategoryRequestDTO medicalItemCategoryRequestDTO, @PathVariable Long id) {
        try {
            MedicalItemCategoryResponseDTO medicalItemCategoryResponseDTOSaved = medicalItemCategoryService.update(medicalItemCategoryRequestDTO, id);
            ApiResponse<MedicalItemCategoryResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), medicalItemCategoryResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<MedicalItemCategoryResponseDTO> badRequestResponse =
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (Exception e) {
            ApiResponse<MedicalItemCategoryResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update Medical Item Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteMedicalItemCategory(@PathVariable Long id, @RequestBody MedicalItemCategoryRequestDTO medicalItemCategoryRequestDTO) {
        try {
            medicalItemCategoryService.softDeleteMedicalItemCategory(id,medicalItemCategoryRequestDTO.getDeletedBy());

            ApiResponse<MedicalItemCategoryResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), "Medical Item Level soft deleted", null);
            return ResponseEntity.ok(successResponse);
        }catch (IllegalArgumentException e) {
            ApiResponse<MedicalItemCategoryResponseDTO> badRequestResponse =
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (IllegalStateException e){
            ApiResponse<MedicalItemCategoryResponseDTO> conflictResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflictResponse);
        } catch (Exception e) {
            ApiResponse<MedicalItemCategoryResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to soft delete Medical Item Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteMedicalItemCategory(@PathVariable Long id) {
        try{
            Optional<MedicalItemCategoryResponseDTO> medicalItemCategoryResponseDTO = medicalItemCategoryService.findById(id);

            if (medicalItemCategoryResponseDTO.isEmpty()) {
                ApiResponse<MedicalItemCategoryResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Medical Item Level not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            medicalItemCategoryService.deleteById(id);
            ApiResponse<MedicalItemCategoryResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "Medical Item Level deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<MedicalItemCategoryResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete Medical Item Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

}
