package com._a.backend.controllers;

import com._a.backend.dtos.requests.RegistrationRequestDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.RegistrationServiceImpl;
import com._a.backend.services.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
@CrossOrigin("*")
public class RegistrationController {
    @Autowired
    private RegistrationServiceImpl registrationService;

    @PostMapping("")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody RegistrationRequestDTO registrationRequestDTO){
        try {
            registrationService.sendOtp(registrationRequestDTO);
            ApiResponse<?> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), "OTP has been send", null);
            return ResponseEntity.ok(successResponse);
        }catch (IllegalArgumentException e){
            ApiResponse<?> existsResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(existsResponse);
        }catch (Exception e){
            ApiResponse<?> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }



}
