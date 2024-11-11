package com._a.backend.controllers;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.DoctorTreatmentRequestDTO;
import com._a.backend.dtos.responses.DoctorTreatmentResponseDTO;
import com._a.backend.entities.DoctorTreatment;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.DoctorTreatmentServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/doctor/doctor-treatment")
@CrossOrigin("*")
public class DoctorTreatmentController {
  
  @Autowired
  private DoctorTreatmentServiceImpl doctorTreatmentService;

  @GetMapping("")
  public ResponseEntity<?> getAllDoctorTreatment() {
      try {
        List<DoctorTreatmentResponseDTO> doctorTreatmentResponseDTOs = doctorTreatmentService.findAll();
        ApiResponse<List<DoctorTreatmentResponseDTO>> successResponse =
            new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorTreatmentResponseDTOs);
        return ResponseEntity.ok(successResponse);
      } catch (Exception e) {
        ApiResponse<String> errorResponse = new ApiResponse<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
      }
  }

  @GetMapping("/treatment/{id}")
  public ResponseEntity<?> getDoctorTreatmentById(@PathVariable Long id) {
    try {
      Optional<DoctorTreatmentResponseDTO> doctorTreatment = doctorTreatmentService.findById(id);
      if (doctorTreatment.isEmpty()) {
        ApiResponse<DoctorTreatmentResponseDTO> notFoundResponse =
            new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor treatment not found", null);
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
      } else {
        ApiResponse<Optional<DoctorTreatmentResponseDTO>> successResponse =
            new ApiResponse<Optional<DoctorTreatmentResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorTreatment);
          return ResponseEntity.status(HttpStatus.OK).body(successResponse);
      }
    } catch (Exception e) {
      ApiResponse<Optional<DoctorTreatmentResponseDTO>> errorResponse =
          new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<?> getDoctorTreatmentByDoctorId(@PathVariable Long id) {
      try {
        List<DoctorTreatmentResponseDTO> doctorTreatment = doctorTreatmentService.findByDocterId(id);
        if (doctorTreatment.isEmpty()) {
          ApiResponse<DoctorTreatmentResponseDTO> notFoundResponse =
            new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor treatment not found", null);
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }
        else{
          ApiResponse<List<DoctorTreatmentResponseDTO>> successResponse =
            new ApiResponse<List<DoctorTreatmentResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorTreatment);
          return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }
      } catch (Exception e) {
        ApiResponse<List<DoctorTreatmentResponseDTO>> errorResponse =
          new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
      }
  }
  
  @PostMapping("")
  public ResponseEntity<?> saveDoctorTreatment(@RequestBody DoctorTreatmentRequestDTO doctorTreatmentRequestDTO) {
      try {
        DoctorTreatmentResponseDTO doctorTreatmentResponseDTO = doctorTreatmentService.save(doctorTreatmentRequestDTO);
        ApiResponse<DoctorTreatmentResponseDTO> successResponse =
            new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorTreatmentResponseDTO);
        return ResponseEntity.ok(successResponse);
          } catch (Exception e) {
        ApiResponse<DoctorTreatmentResponseDTO> errorResponse =
          new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save doctor treatment", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
      
      }
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<?> updateDoctorTreatment(@PathVariable Long id, @RequestBody DoctorTreatmentRequestDTO doctorTreatmentRequestDTO) {
      try {
        Optional<DoctorTreatmentResponseDTO> doctorTreatmentResponseDTO = doctorTreatmentService.findById(id);
        if (doctorTreatmentResponseDTO.isEmpty()) {
          ApiResponse<DoctorTreatmentResponseDTO> notFoundResponse =
            new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor treatment not found", null);
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }
        DoctorTreatmentResponseDTO doctorTreatmentResponseDTOSaved = doctorTreatmentService.update(doctorTreatmentRequestDTO, id);
        ApiResponse<DoctorTreatmentResponseDTO> successResponse =
          new ApiResponse<DoctorTreatmentResponseDTO>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorTreatmentResponseDTOSaved);
        return ResponseEntity.ok(successResponse);
      } catch (Exception e) {
        ApiResponse<DoctorTreatmentResponseDTO> errorResponse =
          new ApiResponse<DoctorTreatmentResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update doctor treatment", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
      }
  }

  @DeleteMapping("/hard-delete/{id}")
  public ResponseEntity<?> deleteDoctorTreatment(@PathVariable Long id) {
    try {
      doctorTreatmentService.deleteById(id);
      ApiResponse<DoctorTreatmentResponseDTO> successResponse =
        new ApiResponse<>(HttpStatus.OK.value(), "doctor treatment deleted", null);
      return ResponseEntity.ok(successResponse);
    } catch (Exception e) {
      ApiResponse<DoctorTreatmentResponseDTO> errorResponse =
        new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete doctor treatment", null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
    }
  }

  // Soft delete
  @PatchMapping("/delete/{id}")
  public ResponseEntity<?> softDeleteDoctorTreatment(@PathVariable Long id) {
    try {
      Optional<DoctorTreatmentResponseDTO> doctorTreatmentResponseDTO = doctorTreatmentService.findById(id);
      if(doctorTreatmentResponseDTO.isEmpty()){
        ApiResponse<DoctorTreatmentResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor treatment not found", null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
      }
      doctorTreatmentService.softDeleteById(id);
      ApiResponse<DoctorTreatmentResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "doctor treatment deleted", null);
      return ResponseEntity.ok(successResponse);
    } catch (Exception e) {
      ApiResponse<DoctorTreatmentResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete doctor treatment", null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
    }
  }
  

}
