package com._a.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.responses.AppointmentMedicalFacilitiesResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.AppointmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/patient/appointment")
public class AppoinmentController {
  @Autowired
  private AppointmentService appointmentService;

  @GetMapping({ "/doctor-medical-facilities/{doctorId}", "/doctor-medical-facilities/{doctorId}/" })
  public ResponseEntity<ApiResponse<AppointmentMedicalFacilitiesResponseDTO>> getMethodName(
      @PathVariable Long doctorId) {
    return new ResponseEntity<>(
        ApiResponse.success(200, appointmentService.getMedicalFacilitiesByDoctorId(doctorId)),
        HttpStatus.OK);
  }

}
