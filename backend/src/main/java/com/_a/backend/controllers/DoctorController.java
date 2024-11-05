package com._a.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.responses.DoctorResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.DoctorServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/doctor/doctor-self")
@CrossOrigin("*")
public class DoctorController {
  
  @Autowired
  private DoctorServiceImpl doctorService;

  @GetMapping("")
  public ResponseEntity<?> getAllDoctors() {
      try {
        List<DoctorResponseDTO> doctorResponseDTOs = doctorService.findAll();
        ApiResponse<List<DoctorResponseDTO>> successResponse =
          new ApiResponse<List<DoctorResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorResponseDTOs);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
      } catch (Exception e) {
        ApiResponse<List<DoctorResponseDTO>> errorResponse = new ApiResponse<List<DoctorResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getDoctorById(@PathVariable Long id) {
      try {
        Optional<DoctorResponseDTO> doctor = doctorService.findById(id);
        if (doctor.isEmpty()) {
          ApiResponse<DoctorResponseDTO> notFoundResponse =
            new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor not found", null);
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        } else {
          ApiResponse<Optional<DoctorResponseDTO>> successResponse =
            new ApiResponse<Optional<DoctorResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctor);
          return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }
      } catch (Exception e) {
        ApiResponse<DoctorResponseDTO> errorResponse = new ApiResponse<DoctorResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }
  
  
}
