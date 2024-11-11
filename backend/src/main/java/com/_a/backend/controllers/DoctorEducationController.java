package com._a.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.DoctorEducationRequestDTO;
import com._a.backend.dtos.responses.DoctorEducationResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.DoctorEducationServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/doctor/doctor-education")
@CrossOrigin("*")
public class DoctorEducationController {
  
  @Autowired
  private DoctorEducationServiceImpl doctorEducationService;

  @GetMapping("")
  public ResponseEntity<?> getAllDoctorEducations() {
    try {
      List<DoctorEducationResponseDTO> doctorEducationResponseDTOs = doctorEducationService.findAll();
      ApiResponse<List<DoctorEducationResponseDTO>> successResponse = new ApiResponse<List<DoctorEducationResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorEducationResponseDTOs);
      return new ResponseEntity<>(successResponse, HttpStatus.OK);
    } catch (Exception e) {
      ApiResponse<List<DoctorEducationResponseDTO>> errorResponse = new ApiResponse<List<DoctorEducationResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
      return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getDoctorEducationByDoctorId(@PathVariable Long id) {
    try {
            List<DoctorEducationResponseDTO> doctorEducation = doctorEducationService.findByDocterId(id);
            if (doctorEducation.isEmpty()) {
              ApiResponse<DoctorEducationResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor education not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            } else {
              ApiResponse<List<DoctorEducationResponseDTO>> successResponse = 
                new ApiResponse<List<DoctorEducationResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorEducation);
              return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            }
        } catch (Exception e) {
            ApiResponse<List<DoctorEducationResponseDTO>> errorResponse =
              new ApiResponse<List<DoctorEducationResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
  }
  
  @PostMapping("")
  public ResponseEntity<?> saveDoctorEducation(@RequestBody DoctorEducationRequestDTO doctorEducationRequestDTO) {
      try {
        DoctorEducationResponseDTO doctorEducationResponseDTOSaved = doctorEducationService.save(doctorEducationRequestDTO);
        ApiResponse<DoctorEducationResponseDTO> successResponse =
          new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorEducationResponseDTOSaved);
        return ResponseEntity.ok(successResponse);
      } catch (Exception e) {
        ApiResponse<DoctorEducationResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save doctor education", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
      }
  }
  
  @PutMapping("/update/{id}")
  public ResponseEntity<?> updateDoctorEducation(@PathVariable Long id, @RequestBody DoctorEducationRequestDTO doctorEducationRequestDTO) {
      try {
        Optional<DoctorEducationResponseDTO> doctorEducationResponseDTO = doctorEducationService.findById(id);
        if (doctorEducationResponseDTO.isEmpty()) {
          ApiResponse<DoctorEducationResponseDTO> notFoundResponse =
            new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor education not found", null);
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }

        DoctorEducationResponseDTO doctorEducationResponseDTOSaved = doctorEducationService.update(doctorEducationRequestDTO, id);
        ApiResponse<DoctorEducationResponseDTO> successResponse =
          new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorEducationResponseDTOSaved);
        return ResponseEntity.ok(successResponse);
      } catch (Exception e) {
        ApiResponse<DoctorEducationResponseDTO> errorResponse =
              new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update doctor education", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
      }
  }

  @DeleteMapping("/hard-delete/{id}")
  public ResponseEntity<?> hardDeleteDoctorEducation(@PathVariable Long id) {
    try {
      Optional<DoctorEducationResponseDTO> doctorEducationResponDTO = doctorEducationService.findById(id);
      if(doctorEducationResponDTO.isEmpty()) {
        ApiResponse<DoctorEducationResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor education not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
      }

      doctorEducationService.deleteById(id);
      ApiResponse<DoctorEducationResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "doctor education deleted", null);
      return ResponseEntity.ok(successResponse);
    } catch (Exception e) {
      ApiResponse<DoctorEducationResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete doctor education", null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
    }
  }

  // soft delete
  @PutMapping("/delete/{id}")
  public ResponseEntity<?> softDeleteDoctorEducation(@PathVariable Long id) {
      try {
        Optional<DoctorEducationResponseDTO> doctorEducationResponseDTO = doctorEducationService.findById(id);
        if(doctorEducationResponseDTO.isEmpty()) {
          ApiResponse<DoctorEducationResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor education not found", null);
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }
        doctorEducationService.softDeleteById(id);
        ApiResponse<DoctorEducationResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "doctor education deleted", null);
        return ResponseEntity.ok(successResponse);
      } catch (Exception e) {
        ApiResponse<DoctorEducationResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete doctor education", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
      }
  }

}
