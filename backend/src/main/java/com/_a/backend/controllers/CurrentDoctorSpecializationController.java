package com._a.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.responses.CurrentDoctorSpecializationResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.CurrentDoctorSpecializationServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/doctor/current-doctor-specialization")
@CrossOrigin("*")
public class CurrentDoctorSpecializationController {
  
  @Autowired
  private CurrentDoctorSpecializationServiceImpl currentDoctorSpecializationService;

  @GetMapping("")
  public ResponseEntity<?> getAllCurrentDoctorSpecialization() {
      try {
        List<CurrentDoctorSpecializationResponseDTO> currentDoctorSpecializationResponseDTO = currentDoctorSpecializationService.findAll();
        ApiResponse<List<CurrentDoctorSpecializationResponseDTO>> successResponse = 
          new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), currentDoctorSpecializationResponseDTO);
        return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
        ApiResponse<List<CurrentDoctorSpecializationResponseDTO>> errorResponse = 
          new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
      }
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<?> getCurrentDoctorSpecializationById(@PathVariable Long id) {
    try {
      List<CurrentDoctorSpecializationResponseDTO> currentDoctorSpecialization = currentDoctorSpecializationService.findByDoctorId(id);
      if (currentDoctorSpecialization.isEmpty()) {
        ApiResponse<CurrentDoctorSpecializationResponseDTO> notFoundResponse =
          new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "current doctor specialization not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
      } else {
        ApiResponse <List<CurrentDoctorSpecializationResponseDTO>> successResponse =
          new ApiResponse <List<CurrentDoctorSpecializationResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), currentDoctorSpecialization);
        return ResponseEntity.status(HttpStatus.OK).body(successResponse);
      }
    } catch (Exception e) {
      ApiResponse<CurrentDoctorSpecializationResponseDTO> errorResponse =
        new ApiResponse<CurrentDoctorSpecializationResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
      return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
