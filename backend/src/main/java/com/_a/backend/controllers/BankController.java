package com._a.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.BankRequestDTO;
import com._a.backend.dtos.responses.BankResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.BankRepository;
import com._a.backend.services.impl.BankServiceImpl;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/bank")
public class BankController {

    @Autowired
    private BankServiceImpl bankService;

    @Autowired
    private BankRepository bankRepository;

    @GetMapping("")
    public ResponseEntity<?> findAllBanks() {
        try {
            List<BankResponseDTO> bankResponseDTOs = bankService.findAll();
            ApiResponse<List<BankResponseDTO>> successResponse = new ApiResponse<List<BankResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bankResponseDTOs);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<BankResponseDTO>> errorResponse = new ApiResponse<List<BankResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveBank(@Valid @RequestBody BankRequestDTO bankRequestDTO) {
        if (bankRepository.existsByName(bankRequestDTO.getName())) {
            ApiResponse<BankResponseDTO> alreadyExistResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(), "Bank already exist", null);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(alreadyExistResponse);
        }
        try {
            BankResponseDTO bankResponseDTOSaved = bankService.save(bankRequestDTO);
            ApiResponse<BankResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bankResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<BankResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save Bank", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBank(@Valid @RequestBody BankRequestDTO bankRequestDTO, @PathVariable Long id) {
        try {
            Optional<BankResponseDTO> bankResponseDTO = bankService.findById(id);
            if (bankResponseDTO.isEmpty()) {
                ApiResponse<BankResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Bank not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
            BankResponseDTO bankResponseDTOSaved = bankService.update(bankRequestDTO, id);
            ApiResponse<BankResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bankResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<BankResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update Bank", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteBank(@PathVariable Long id) {
        try {
            Optional<BankResponseDTO> bankResponseDTO = bankService.findById(id);
            if (bankResponseDTO.isEmpty()) {
                ApiResponse<BankResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Bank not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);

            }
            bankService.deleteById(id);
            ApiResponse<BankResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "Bank deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<BankResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete Bank", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

}
