package com._a.backend.controllers;

import com._a.backend.dtos.requests.EmailRequestDTO;
import com._a.backend.dtos.requests.PasswordRegistRequestDTO;
import com._a.backend.dtos.requests.RegistrationRequestDTO;
import com._a.backend.dtos.requests.VerifyOtpRequestDto;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.RegistrationServiceImpl;
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
    public ResponseEntity<ApiResponse<RegistrationRequestDTO>> registration(@RequestBody @Valid RegistrationRequestDTO registrationRequestDTO) {
        try {
            registrationService.registration(registrationRequestDTO);

            ApiResponse<RegistrationRequestDTO> successResponse =
                    new ApiResponse<>(HttpStatus.CREATED.value(), "Berhasil mendaftarkan akun", null);
            return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
        }catch (Exception e){
            ApiResponse<RegistrationRequestDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody EmailRequestDTO emailRequestDTO) throws Exception {
        registrationService.sendOtp(emailRequestDTO);
        ApiResponse<?> successResponse =
                new ApiResponse<>(HttpStatus.OK.value(), "OTP telah terkirim", null);
        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto verifyOtpRequestDto) throws Exception{
        registrationService.verifyOtp(verifyOtpRequestDto);

        ApiResponse<?> successResponse =  new ApiResponse<>(HttpStatus.OK.value(), "Verifikasi berhasil, silahkan lanjut ke step berikutnya.", null);
        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/verify-password")
    public ResponseEntity<?> verifyPassword(@Valid @RequestBody PasswordRegistRequestDTO passwordRegistRequestDTO) throws Exception {
       registrationService.confirmPassword(passwordRegistRequestDTO);

       ApiResponse<?> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "Password berhasil di buat.", null);
       return ResponseEntity.ok(successResponse);
    }




}
