package com._a.backend.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.projections.AppointmentExceededDateProjectionDTO;
import com._a.backend.dtos.requests.AppointmentRequestDTO;
import com._a.backend.dtos.responses.AppointmentCustomerDocterOfficeScheduleResponseDTO;
import com._a.backend.dtos.responses.AppointmentMedicalFacilitiesResponseDTO;
import com._a.backend.dtos.responses.AppointmentResponseDTO;
import com._a.backend.dtos.responses.DoctorOfficeScheduleResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.AppointmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/patient/appointment")
public class AppoinmentController {
  @Autowired
  private AppointmentService appointmentService;

  @GetMapping({ "/doctor-medical-facilities/{doctorId}", "/doctor-medical-facilities/{doctorId}/" })
  public ResponseEntity<ApiResponse<AppointmentMedicalFacilitiesResponseDTO>> getMedicalFacilities(
      @PathVariable Long doctorId) {
    return new ResponseEntity<>(
        ApiResponse.success(200, appointmentService.getMedicalFacilitiesByDoctorId(doctorId)),
        HttpStatus.OK);
  }

  @GetMapping({ "/exceeded-date/{doctorId}", "/exceeded-date/{doctorId}/" })
  public ResponseEntity<ApiResponse<List<AppointmentExceededDateProjectionDTO>>> getExceededAppointmentDate(
      @PathVariable Long doctorId) {
    return new ResponseEntity<>(
        ApiResponse.success(200, appointmentService.getExceededDatesByDoctorId(doctorId)),
        HttpStatus.OK);
  }

  @GetMapping({ "doctor-office-schedule/{doctorId}", "doctor-office-schedule/{doctorId}/" })
  public ResponseEntity<ApiResponse<List<DoctorOfficeScheduleResponseDTO>>> getSchedule(@PathVariable Long doctorId) {
    return new ResponseEntity<>(
        ApiResponse.success(200, appointmentService.getDoctorOfficeSchedulesByDoctorId(doctorId)),
        HttpStatus.OK);
  }

  @GetMapping({ "/isExceeded", "/isExceeded/" })
  public Boolean isAppointmentCountExceeded(
      @RequestParam("appointmentDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate appointmentDate,
      @RequestParam("doctorOfficeScheduleId") Long doctorOfficeScheduleId) {

    return appointmentService.isAppointmentCountExceeded(appointmentDate, doctorOfficeScheduleId);
  }

  @PostMapping({ "", "/" })
  public ResponseEntity<ApiResponse<AppointmentResponseDTO>> postMethodName(
      @RequestBody AppointmentRequestDTO requestDTO) {

    return new ResponseEntity<>(ApiResponse.success(201, appointmentService.create(requestDTO)), HttpStatus.CREATED);
  }

  @GetMapping({ "/members-doctor-office-schedule", "/members-doctor-office-schedule/" })
  public ResponseEntity<ApiResponse<List<AppointmentCustomerDocterOfficeScheduleResponseDTO>>> getAllCustomerDoctorOfficeScheduleForCurrentUser() {

    return new ResponseEntity<>(
        ApiResponse.success(200, appointmentService.getAllCustomerDoctorOfficeScheduleByUserId()), HttpStatus.OK);
  }

}
