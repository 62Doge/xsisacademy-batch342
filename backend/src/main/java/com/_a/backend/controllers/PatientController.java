package com._a.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.PatientRequestDTO;
import com._a.backend.dtos.responses.PatientResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.DumpAuthServiceImpl;
import com._a.backend.services.impl.PatientServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/patient")
@CrossOrigin("http://localhost:9002")
public class PatientController {

    @Autowired
    PatientServiceImpl patientService;

    @Autowired
    DumpAuthServiceImpl dumpAuthServiceImpl;

    @GetMapping("/active")
    public ResponseEntity<?> getActivePatientPages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "fullname") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        try {
            Long parentBiodataId = dumpAuthServiceImpl.getDetails().getBiodataId();
            Pageable pageable = PageRequest.of(page, size);
            if (sortBy.equalsIgnoreCase("fullname")) {
                if (sortDir.equalsIgnoreCase("ASC")) {
                    pageable = PageRequest.of(page, size, Sort.by("b.fullname").ascending());
                } else if (sortDir.equalsIgnoreCase("DESC")) {
                    pageable = PageRequest.of(page, size, Sort.by("b.fullname").descending());
                }
            } else if (sortBy.equalsIgnoreCase("age")) {
                if (sortDir.equalsIgnoreCase("ASC")) {
                    pageable = PageRequest.of(page, size, Sort.by("c.dob").descending());
                } else if (sortDir.equalsIgnoreCase("DESC")) {
                    pageable = PageRequest.of(page, size, Sort.by("c.dob").ascending());
                }
            }
            Page<PatientResponseDTO> patient = patientService.findActivePages(parentBiodataId, pageable);
            ApiResponse<Page<PatientResponseDTO>> successResponse = new ApiResponse<Page<PatientResponseDTO>>(
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), patient);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<PatientResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Patient not found", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getPatientByQuery(
        @PathVariable String name,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "fullname") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDir) {
        try {
            Long parentBiodataId = dumpAuthServiceImpl.getDetails().getBiodataId();
            Pageable pageable = PageRequest.of(page, size);
            if (sortBy.equalsIgnoreCase("fullname")) {
                if (sortDir.equalsIgnoreCase("ASC")) {
                    pageable = PageRequest.of(page, size, Sort.by("b.fullname").ascending());
                } else if (sortDir.equalsIgnoreCase("DESC")) {
                    pageable = PageRequest.of(page, size, Sort.by("b.fullname").descending());
                }
            } else if (sortBy.equalsIgnoreCase("age")) {
                if (sortDir.equalsIgnoreCase("ASC")) {
                    pageable = PageRequest.of(page, size, Sort.by("c.dob").descending());
                } else if (sortDir.equalsIgnoreCase("DESC")) {
                    pageable = PageRequest.of(page, size, Sort.by("c.dob").ascending());
                }
            }
            Page<PatientResponseDTO> patient = patientService.findActivePagesByName(name, parentBiodataId, pageable);
            ApiResponse<Page<PatientResponseDTO>> successResponse = new ApiResponse<Page<PatientResponseDTO>>(
                    HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), patient);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<PatientResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Patient not found", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPatientByID(@PathVariable Long id) {
        try {
            PatientResponseDTO patient = patientService.findById(id);
            ApiResponse<PatientResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), patient);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<PatientResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Patient not found", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getPatientsByParentBiodataId() {
        try {
            Long parentBiodataId = dumpAuthServiceImpl.getDetails().getBiodataId();
            List<PatientResponseDTO> patientResponseDTOs = patientService.findByParentBiodataId(parentBiodataId);
            ApiResponse<List<PatientResponseDTO>> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), patientResponseDTOs);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<PatientResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Patient not found", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> savePatient(@RequestBody PatientRequestDTO patientRequestDTO) {
        try {
            Long parentBiodataId = dumpAuthServiceImpl.getDetails().getBiodataId();
            PatientResponseDTO patientResponseDTOSaved = patientService.save(parentBiodataId, patientRequestDTO);
            ApiResponse<PatientResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), patientResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<PatientResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to save Patient", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePatient(@PathVariable Long id, @RequestBody PatientRequestDTO patientRequestDTO) {
        try {
            Long parentBiodataId = dumpAuthServiceImpl.getDetails().getBiodataId();
            PatientResponseDTO patientResponseDTO = patientService.update(id, parentBiodataId, patientRequestDTO);
            ApiResponse<PatientResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(),
                    HttpStatus.OK.getReasonPhrase(), patientResponseDTO);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<PatientResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Failed to update Patient", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    // for deletion use delete api in CustomerMember

}
