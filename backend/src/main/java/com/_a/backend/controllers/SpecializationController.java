package com._a.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.responses.SpecializationResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.SpecializationServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/doctor/specialization")
@CrossOrigin("*")
public class SpecializationController {
  
  @Autowired
  SpecializationServiceImpl specializationService;

  @GetMapping("")
  public ResponseEntity<?> getAllSpecialization() {
      try {
        List<SpecializationResponseDTO> specializationResponseDTOs = specializationService.findAll();
        ApiResponse<List<SpecializationResponseDTO>> successResponse =
          new ApiResponse<List<SpecializationResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), specializationResponseDTOs);
        return ResponseEntity.ok(successResponse);
      } catch (Exception e) {
        ApiResponse<List<SpecializationResponseDTO>> errorResponse =
          new ApiResponse<List<SpecializationResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
      }
  }
  
}
