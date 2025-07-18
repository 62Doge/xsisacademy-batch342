package com._a.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.CurrentDoctorSpecializationRequestDTO;
import com._a.backend.dtos.responses.CurrentDoctorSpecializationResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.CurrentDoctorSpecializationServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




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

  @PostMapping("")
  public ResponseEntity<?> saveCurrentDoctorSpecialization(@RequestBody CurrentDoctorSpecializationRequestDTO doctorSpecializationRequestDTO) {
    try {
      CurrentDoctorSpecializationResponseDTO currentDoctorSpecializationResponseDTOSaved = currentDoctorSpecializationService.save(doctorSpecializationRequestDTO);
      ApiResponse<CurrentDoctorSpecializationResponseDTO> successResponse =
        new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), currentDoctorSpecializationResponseDTOSaved);
      return ResponseEntity.ok(successResponse);
    } catch (Exception e) {
      ApiResponse<CurrentDoctorSpecializationResponseDTO> errorResponse =
        new ApiResponse<CurrentDoctorSpecializationResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
      return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> updateCurrentDoctorSpecialization(@PathVariable Long id, @RequestBody CurrentDoctorSpecializationRequestDTO doctorSpecializationRequestDTO) {
      try {
        List<CurrentDoctorSpecializationResponseDTO> currentDoctorSpecializationResponseDTO = currentDoctorSpecializationService.findByDoctorId(id);
        if (currentDoctorSpecializationResponseDTO.isEmpty()) {
          ApiResponse<CurrentDoctorSpecializationResponseDTO> notFoundResponse =
            new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "current doctor specialization not found", null);
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }

        CurrentDoctorSpecializationResponseDTO currentDoctorSpecializationResponseDTOSaved = currentDoctorSpecializationService.update(doctorSpecializationRequestDTO, id);
        ApiResponse<CurrentDoctorSpecializationResponseDTO> successResponse =
          new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), currentDoctorSpecializationResponseDTOSaved);
        return ResponseEntity.ok(successResponse);
      } catch (Exception e) {
        ApiResponse<CurrentDoctorSpecializationResponseDTO> errorResponse =
          new ApiResponse<CurrentDoctorSpecializationResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update current doctor specialization", null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
}
