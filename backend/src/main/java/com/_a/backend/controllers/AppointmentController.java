package com._a.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.responses.AppointmentResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.AppointmentServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/doctor/appointment")
@CrossOrigin("*")
public class AppointmentController {
  
  @Autowired
  private AppointmentServiceImpl appointmentService;

  @GetMapping("")
  public ResponseEntity<?> getAllAppointments() {
      try {
        List<AppointmentResponseDTO> appointmentResponseDTOs = appointmentService.findAll();
        ApiResponse<List<AppointmentResponseDTO>> successResponse =
          new ApiResponse<List<AppointmentResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), appointmentResponseDTOs);
        return new ResponseEntity<>(successResponse, HttpStatus.OK);
      } catch (Exception e) {
        ApiResponse<List<AppointmentResponseDTO>> errorResponse =
          new ApiResponse<List<AppointmentResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getAppointmentByOfficeId(@PathVariable Long id) {
    try {
      List<AppointmentResponseDTO> appointment = appointmentService.findByOfficeId(id);
      if(appointment.isEmpty()) {
        ApiResponse<List<AppointmentResponseDTO>> notFoundResponse =
          new ApiResponse<List<AppointmentResponseDTO>>(HttpStatus.NOT_FOUND.value(), "appointment not found", null);
        return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
      }else{
      ApiResponse<List<AppointmentResponseDTO>> successResponse =
        new ApiResponse<List<AppointmentResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), appointment);
      return new ResponseEntity<>(successResponse, HttpStatus.OK);
      }
      
      
      
    } catch (Exception e) {
      ApiResponse<Optional<AppointmentResponseDTO>> errorResponse =
        new ApiResponse<Optional<AppointmentResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
      return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  
}
