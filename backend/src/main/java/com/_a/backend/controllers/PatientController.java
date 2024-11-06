package com._a.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.PatientRequestDTO;
import com._a.backend.dtos.responses.PatientResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.PatientServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    PatientServiceImpl patientService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable Long id) {
        try {
            PatientResponseDTO patientResponseDTO = patientService.findById(id);
            ApiResponse<PatientResponseDTO> successResponse =
                new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), patientResponseDTO);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<PatientResponseDTO> errorResponse =
                new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Patient not found", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }
    
    // id -> parent biodata id
    @PostMapping("/{parentBiodataId}")
    public ResponseEntity<?> savePatient(@PathVariable Long parentBiodataId, @RequestBody PatientRequestDTO patientRequestDTO) {
        try {
            PatientResponseDTO patientResponseDTOSaved = patientService.save(parentBiodataId, patientRequestDTO);
            ApiResponse<PatientResponseDTO> successResponse =
                new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), patientResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<PatientResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to save Patient", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody PatientRequestDTO patientRequestDTO) {
        try {
            PatientResponseDTO patientResponseDTO = patientService.update(id, patientRequestDTO);
            ApiResponse<PatientResponseDTO> successResponse = 
                new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), patientResponseDTO);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<PatientResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to update Patient", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    // for deletion use delete api in CustomerMember

}
