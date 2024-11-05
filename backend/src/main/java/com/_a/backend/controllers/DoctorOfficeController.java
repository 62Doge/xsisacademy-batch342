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

import com._a.backend.dtos.requests.DoctorOfficeRequestDTO;
import com._a.backend.dtos.responses.DoctorOfficeResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.DoctorOfficeServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/doctor/doctor-office")
@CrossOrigin("*")
public class DoctorOfficeController {
  
  @Autowired
  private DoctorOfficeServiceImpl doctorOfficeService;

  @GetMapping("")
  public ResponseEntity<?> getAllDoctorOffice() {
      try {
          List<DoctorOfficeResponseDTO> doctorOfficeResponseDTOs = doctorOfficeService.findAll();
          ApiResponse<List<DoctorOfficeResponseDTO>> successResponse =
                  new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorOfficeResponseDTOs);
          return ResponseEntity.ok(successResponse);
      } catch (Exception e) {
          ApiResponse<String> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e.getMessage());
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
      }
  }
  
  @GetMapping("/{id}")
  public ResponseEntity<?> getDoctorOfficeByDoctorId(@PathVariable Long id) {
      try {
        List<DoctorOfficeResponseDTO> doctorOffice = doctorOfficeService.findByDoctorId(id);
        if (doctorOffice.isEmpty()) {
          ApiResponse<DoctorOfficeResponseDTO> notFoundResponse =
            new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor office not found", null);
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }
        else{
          ApiResponse<List<DoctorOfficeResponseDTO>> successResponse =
            new ApiResponse<List<DoctorOfficeResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorOffice);
          return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }
        } catch (Exception e) {
        ApiResponse<List<DoctorOfficeResponseDTO>> errorResponse =
          new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
      }
  }
  
  @PostMapping("")
  public ResponseEntity<?> saveDoctorOffice(@RequestBody DoctorOfficeRequestDTO doctorOfficeRequestDTO) {
      try {
        DoctorOfficeResponseDTO doctorOfficeResponseDTO = doctorOfficeService.save(doctorOfficeRequestDTO);
        ApiResponse<DoctorOfficeResponseDTO> successResponse =
          new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorOfficeResponseDTO);
        return ResponseEntity.ok(successResponse);
      } catch (Exception e) {
        ApiResponse<DoctorOfficeResponseDTO> errorResponse =
          new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save doctor office", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
      }
  }
  
  @PutMapping("/update/{id}")
  public ResponseEntity<?> updateDoctorOffice(@PathVariable Long id, @RequestBody DoctorOfficeRequestDTO doctorOfficeRequestDTO) {
      try {
        Optional<DoctorOfficeResponseDTO> doctorOfficeResponseDTO = doctorOfficeService.findById(id);
        if (doctorOfficeResponseDTO.isEmpty()) {
          ApiResponse<DoctorOfficeResponseDTO> notFoundResponse =
            new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor office not found", null);
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }

        DoctorOfficeResponseDTO doctorOfficeResponseDTOSaved = doctorOfficeService.update(doctorOfficeRequestDTO, id);
        ApiResponse<DoctorOfficeResponseDTO> successResponse =
          new ApiResponse<DoctorOfficeResponseDTO>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), doctorOfficeResponseDTOSaved);
        return ResponseEntity.ok(successResponse);
      } catch (Exception e) {
        ApiResponse<DoctorOfficeResponseDTO> errorResponse = 
          new ApiResponse<DoctorOfficeResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update doctor office", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
      }
  }

  @DeleteMapping("/hard-delete/{id}")
  public ResponseEntity<?> deleteDoctorOffice(@PathVariable Long id) {
    try {
      doctorOfficeService.deleteById(id);
      ApiResponse<DoctorOfficeResponseDTO> successResponse =
        new ApiResponse<>(HttpStatus.OK.value(), "doctor office deleted", null);
      return ResponseEntity.ok(successResponse);
    } catch (Exception e) {
      ApiResponse<DoctorOfficeResponseDTO> errorResponse =
        new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete doctor office", null);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
    }
  }

  // soft delete
  @PutMapping("/delete/{id}")
  public ResponseEntity<?> softDeleteDoctorOffice(@PathVariable Long id) {
      try {
        Optional<DoctorOfficeResponseDTO> doctorOfficeResponseDTO = doctorOfficeService.findById(id);
        if(doctorOfficeResponseDTO.isEmpty()){
          ApiResponse<DoctorOfficeResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor office not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }
        doctorOfficeService.softDeleteById(id);
        ApiResponse<DoctorOfficeResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "doctor office deleted", null);
        return ResponseEntity.ok(successResponse);
      } catch (Exception e) {
        ApiResponse<DoctorOfficeResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete doctor office", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
      }
  }
}
