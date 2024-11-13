package com._a.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.responses.DoctorDetailsEducationResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsHeaderResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsOfficeHistoryResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsOfficeLocationResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsPriceStartResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsScheduleResponseDTO;
import com._a.backend.dtos.responses.DoctorDetailsTreatmentResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.DoctorDetailsServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/doctor/details")
@CrossOrigin("http://localhost:9002")
public class DoctorDetailsController {

    @Autowired
    DoctorDetailsServiceImpl doctorDetailsServiceImpl;

    @GetMapping("/header/{doctorId}")
    public ResponseEntity<?> getHeader(@PathVariable Long doctorId) {
        try {
            DoctorDetailsHeaderResponseDTO responseDTO = doctorDetailsServiceImpl.getHeader(doctorId);
            ApiResponse<DoctorDetailsHeaderResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), responseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<DoctorDetailsHeaderResponseDTO> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch Doctor Details: Header", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/treatment/{doctorId}")
    public ResponseEntity<?> getTreatment(@PathVariable Long doctorId) {
        try {
            DoctorDetailsTreatmentResponseDTO responseDTO = doctorDetailsServiceImpl.getTreatment(doctorId);
            ApiResponse<DoctorDetailsTreatmentResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), responseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<DoctorDetailsTreatmentResponseDTO> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch Doctor Details: Treatments", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/education/{doctorId}")
    public ResponseEntity<?> getEducation(@PathVariable Long doctorId) {
        try {
            DoctorDetailsEducationResponseDTO responseDTO = doctorDetailsServiceImpl.getEducation(doctorId);
            ApiResponse<DoctorDetailsEducationResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), responseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<DoctorDetailsEducationResponseDTO> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch Doctor Details: Education", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/office-history/{doctorId}")
    public ResponseEntity<?> getOfficeHistory(@PathVariable Long doctorId) {
        try {
            DoctorDetailsOfficeHistoryResponseDTO responseDTO = doctorDetailsServiceImpl.getOfficeHistory(doctorId);
            ApiResponse<DoctorDetailsOfficeHistoryResponseDTO> successResponse = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), responseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<DoctorDetailsOfficeHistoryResponseDTO> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch Doctor Details: Office History", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/office-location/schedule/{medicalFacilityId}/{doctorId}")
    public ResponseEntity<?> getSchedule(@PathVariable Long medicalFacilityId, @PathVariable Long doctorId) {
        try {
            DoctorDetailsScheduleResponseDTO responseDTO = doctorDetailsServiceImpl.getSchedule(medicalFacilityId, doctorId);
            ApiResponse<DoctorDetailsScheduleResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), responseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<DoctorDetailsScheduleResponseDTO> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch Doctor Details: Schedule", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/office-location/price-start/{medicalFacilityId}/{doctorId}")
    public ResponseEntity<?> getPriceStart(@PathVariable Long medicalFacilityId, @PathVariable Long doctorId) {
        try {
            DoctorDetailsPriceStartResponseDTO responseDTO = doctorDetailsServiceImpl.getPriceStart(medicalFacilityId, doctorId);
            ApiResponse<DoctorDetailsPriceStartResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), responseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<DoctorDetailsPriceStartResponseDTO> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch Doctor Details: Price Start", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/office-location/{doctorId}")
    public ResponseEntity<?> getOfficeLocation(@PathVariable Long doctorId) {
        try {
            DoctorDetailsOfficeLocationResponseDTO responseDTO = doctorDetailsServiceImpl.getOfficeLocation(doctorId);
            ApiResponse<DoctorDetailsOfficeLocationResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), responseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<DoctorDetailsOfficeLocationResponseDTO> errorResponse = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to fetch Doctor Details: Office Location", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
