package com._a.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.BankRequestDTO;
import com._a.backend.dtos.responses.BankResponseDTO;
import com._a.backend.entities.Bank;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.BankRepository;
import com._a.backend.services.impl.BankServiceImpl;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/admin/bank")
@CrossOrigin("http://localhost:9002")
public class BankController {

    @Autowired
    private BankServiceImpl bankService;

    @Autowired
    private BankRepository bankRepository;

    @GetMapping("")
    public ResponseEntity<?> getAllBanks() {
        try {
            List<BankResponseDTO> bankResponseDTOs = bankService.findAll();
            ApiResponse<List<BankResponseDTO>> successResponse = new ApiResponse<List<BankResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bankResponseDTOs);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<BankResponseDTO>> errorResponse = new ApiResponse<List<BankResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<?> getActiveBankPages(
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "name") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDir
        ) {
        try {
            Page<Bank> banks = bankService.findActivePages(page, size, sortBy, sortDir);
            ApiResponse<Page<Bank>> successResponse = new ApiResponse<Page<Bank>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), banks);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<BankResponseDTO>> errorResponse = new ApiResponse<List<BankResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/active/data")
    public ResponseEntity<?> getAllActiveBanks() {
        try {
            List<BankResponseDTO> bankResponseDTOs = bankService.findAllActive();
            ApiResponse<List<BankResponseDTO>> successResponse = new ApiResponse<List<BankResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bankResponseDTOs);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<BankResponseDTO>> errorResponse = new ApiResponse<List<BankResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBankByID(@PathVariable Long id) {
        try {
            Optional<BankResponseDTO> bank = bankService.findById(id);
            if (bank.isPresent()) {
                ApiResponse<BankResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bank.get());
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                ApiResponse<BankResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Bank not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        } catch (Exception e) {
            ApiResponse<List<BankResponseDTO>> errorResponse = new ApiResponse<List<BankResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/active/data/name/{name}")
    public ResponseEntity<?> getBankByName(@PathVariable String name) {
        try {
            List<BankResponseDTO> bankResponseDTOs = bankService.findByName(name);
            ApiResponse<List<BankResponseDTO>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bankResponseDTOs);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<List<BankResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getBankByName(
        @PathVariable String name,
        @RequestParam(defaultValue = "0") int page, 
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "name") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortDir
        ) {
        try {
            Page<Bank> bank = bankService.findByNameContaining(name, page, size, sortBy, sortDir);
            ApiResponse<Page<Bank>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bank);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        } catch (Exception e) {
            ApiResponse<List<BankResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveBank(@Valid @RequestBody BankRequestDTO bankRequestDTO) {
        if (bankRepository.existsByNameIgnoreCaseAndIsDeleteFalse(bankRequestDTO.getName())) {
            ApiResponse<BankResponseDTO> alreadyExistResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(), "Bank already exist", null);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(alreadyExistResponse);
        }
        if (bankRepository.existsByVaCodeAndIsDeleteFalse(bankRequestDTO.getVaCode())) {
            ApiResponse<BankResponseDTO> alreadyExistResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(), "VA Code already exist", null);
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
            Optional<Bank> existingBankName = bankRepository.findByNameIgnoreCaseAndIsDeleteFalse(bankRequestDTO.getName());
            if (existingBankName.isPresent() && !existingBankName.get().getId().equals(id)) {
                ApiResponse<BankResponseDTO> conflictResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(), "Bank name already exists", null);
                return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(conflictResponse);
            }
            Optional<Bank> existingBankVaCode = bankRepository.findByVaCodeAndIsDeleteFalse(bankRequestDTO.getVaCode());
            if (existingBankVaCode.isPresent() && !existingBankVaCode.get().getId().equals(id)) {
                ApiResponse<BankResponseDTO> conflictResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(), "VA Code already exists", null);
                return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(conflictResponse);
            }
            BankResponseDTO bankResponseDTOSaved = bankService.update(bankRequestDTO, id);
            ApiResponse<BankResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), bankResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<BankResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update Bank", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        } 
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<?> softDeleteBank(@PathVariable Long id) {
        try {
            Optional<BankResponseDTO> bankResponseDTO = bankService.findById(id);
            if (bankResponseDTO.isEmpty()) {
                ApiResponse<BankResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Bank not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
            bankService.softDeleteById(id);
            ApiResponse<BankResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "Bank deleted successfully", null);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<BankResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete Bank", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @DeleteMapping("/hard-delete/{id}")
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
