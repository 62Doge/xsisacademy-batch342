package com._a.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.BloodGroupRequestDTO;
import com._a.backend.dtos.responses.BloodGroupResponseDTO;
import com._a.backend.entities.BloodGroup;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.BloodGroupRepository;
import com._a.backend.services.impl.BloodGroupServiceImpl;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/admin/blood-group")
@CrossOrigin("http://localhost:9002")
public class BloodGroupController {
    
    @Autowired
    BloodGroupServiceImpl bloodGroupService;

    @Autowired
    BloodGroupRepository bloodGroupRepository;

    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveBloodGroups() {
        try {
            List<BloodGroupResponseDTO> bloodGroupResponseDTOs = bloodGroupService.findAllActive();
            ApiResponse<List<BloodGroupResponseDTO>> successResponse = new ApiResponse<List<BloodGroupResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bloodGroupResponseDTOs);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<BloodGroupResponseDTO>> errorResponse = new ApiResponse<List<BloodGroupResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("")
    public ResponseEntity<?> getAllBloodGroups() {
        try {
            List<BloodGroupResponseDTO> bloodGroupResponseDTOs = bloodGroupService.findAll();
            ApiResponse<List<BloodGroupResponseDTO>> successResponse = new ApiResponse<List<BloodGroupResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bloodGroupResponseDTOs);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<BloodGroupResponseDTO>> errorResponse = new ApiResponse<List<BloodGroupResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBloodGroupById(@PathVariable Long id) {
        try {
            Optional<BloodGroupResponseDTO> bloodGroup = bloodGroupService.findById(id);
            if (bloodGroup.isPresent()) {
                ApiResponse<BloodGroupResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bloodGroup.get());
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                ApiResponse<BloodGroupResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Blood Group not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        } catch (Exception e) {
            ApiResponse<List<BloodGroupResponseDTO>> errorResponse = new ApiResponse<List<BloodGroupResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/code/{query}")
    public ResponseEntity<?> getCodeByQuery(@PathVariable String query) {
        try {
            List<BloodGroupResponseDTO> responseDTOs = bloodGroupService.findByCode(query);
            ApiResponse<List<BloodGroupResponseDTO>> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), responseDTOs);
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<List<BloodGroupResponseDTO>> errorResponse = new ApiResponse<List<BloodGroupResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
    @PostMapping("")
    public ResponseEntity<?> createBloodGroup(@Valid @RequestBody BloodGroupRequestDTO bloodGroupRequestDTO) {
        if (bloodGroupRepository.existsByCodeIgnoreCaseAndIsDeleteFalse(bloodGroupRequestDTO.getCode())) {
            ApiResponse<BloodGroupResponseDTO> alreadyExistResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(), "Blood Group already exists", null);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(alreadyExistResponse);
        }
        if (bloodGroupRequestDTO.getCode().length() > 5) {
            ApiResponse<BloodGroupResponseDTO> characterLimitResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Code input exceed character limit of 5", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(characterLimitResponse);
        }
        try {
            BloodGroupResponseDTO bloodGroupResponseDTOSaved = bloodGroupService.save(bloodGroupRequestDTO);
            ApiResponse<BloodGroupResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bloodGroupResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<BloodGroupResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save Blood Group", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBloodGroup(@Valid @RequestBody BloodGroupRequestDTO bloodGroupRequestDTO, @PathVariable Long id) {
        try {
            Optional<BloodGroupResponseDTO> bloodGroupResponseDTO = bloodGroupService.findById(id);
            if (bloodGroupResponseDTO.isEmpty()) {
                ApiResponse<BloodGroupResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Blood Group not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
            if (bloodGroupRequestDTO.getCode().length() > 5) {
                ApiResponse<BloodGroupResponseDTO> characterLimitResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Code input exceed character limit of 5", null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(characterLimitResponse);
            }
            Optional<BloodGroup> existingBloodGroupCode = bloodGroupRepository.findByCodeIgnoreCaseAndIsDeleteFalse(bloodGroupRequestDTO.getCode());
            if (existingBloodGroupCode.isPresent() && !existingBloodGroupCode.get().getId().equals(id)) {
                ApiResponse<BloodGroupResponseDTO> conflictResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(), "Blood Group already exists", null);
                return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(conflictResponse);
            }
            BloodGroupResponseDTO bloodGroupResponseDTOSaved = bloodGroupService.update(bloodGroupRequestDTO, id);
            ApiResponse<BloodGroupResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bloodGroupResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<BloodGroupResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save Blood Group", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteBloodGroup(@PathVariable Long id) {
        try {
            Optional<BloodGroupResponseDTO> bloodGroupResponseDTO = bloodGroupService.findById(id);
            if (bloodGroupResponseDTO.isEmpty()) {
                ApiResponse<BloodGroupResponseDTO> notFoundResponse = new ApiResponse<BloodGroupResponseDTO>(HttpStatus.NOT_FOUND.value(), "Blood Group not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
            bloodGroupService.softDeleteById(id);
            ApiResponse<BloodGroupResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<BloodGroupResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save Blood Group", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

}
