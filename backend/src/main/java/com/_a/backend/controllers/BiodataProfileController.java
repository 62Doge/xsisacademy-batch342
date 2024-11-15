package com._a.backend.controllers;

import com._a.backend.dtos.requests.EmailRequestDTO;
import com._a.backend.dtos.requests.PasswordUpdateRequestDTO;
import com._a.backend.dtos.requests.PersonalDataRequestDTO;
import com._a.backend.dtos.requests.VerifyOtpRequestDto;
import com._a.backend.dtos.responses.BiodataResponseDTO;
import com._a.backend.dtos.responses.PersonalDataResponseDTO;
import com._a.backend.entities.Biodata;
import com._a.backend.exceptions.TokenRequestTooSoonException;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.BiodataServiceImpl;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/biodata-profile")
@CrossOrigin("*")
public class BiodataProfileController {
    @Autowired
    private BiodataServiceImpl biodataService;


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BiodataResponseDTO>> getBiodataProfile(@PathVariable("id") Long id) {
        try {
            BiodataResponseDTO biodataResponseDTO = biodataService.getProfileData(id);

            ApiResponse<BiodataResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), biodataResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }catch (Exception e) {
            ApiResponse<BiodataResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/personal-data/{id}")
    public ResponseEntity<ApiResponse<PersonalDataResponseDTO>> getPersonalData(@PathVariable("id") Long id) {
        try {
            PersonalDataResponseDTO personalDataResponseDTO = biodataService.getPersonalData(id);

            ApiResponse<PersonalDataResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), personalDataResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }catch (Exception e) {
            ApiResponse<PersonalDataResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/personal-data/update/{id}")
    public ResponseEntity<ApiResponse<PersonalDataResponseDTO>> updatePersonalData(
            @Valid @RequestBody PersonalDataRequestDTO personalDataRequestDTO, @PathVariable("id") Long id){
        try {
            PersonalDataResponseDTO personalDataResponseDTO = biodataService.updatePersonalData(id, personalDataRequestDTO);

            ApiResponse<PersonalDataResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), personalDataResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }catch (IllegalArgumentException e){
            ApiResponse<PersonalDataResponseDTO> badRequestResponse =
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        }catch (Exception e) {
            ApiResponse<PersonalDataResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/email/send-otp")
    public ResponseEntity<?> verifyEmail(@Valid @RequestBody EmailRequestDTO emailRequestDTO) {
        try {

            biodataService.sendOtpForEmailUpdate(emailRequestDTO);
            ApiResponse<?> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "OTP telah terkirim", null);
            return ResponseEntity.ok(successResponse);

        } catch (IllegalArgumentException e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (TokenRequestTooSoonException e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(HttpStatus.TOO_MANY_REQUESTS.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
        } catch (Exception e) {
            ApiResponse<?> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/email/verify-otp/{id}")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtpRequestDto verifyOtpRequestDto, @PathVariable Long id) throws Exception{
        biodataService.verifyOtp(id, verifyOtpRequestDto);

        ApiResponse<?> successResponse =  new ApiResponse<>(HttpStatus.OK.value(), "Ubah e-mail berhasil. Silahkan masuk kembali menggunakan e-mail Anda yang baru", null);
        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/confirm-password/{id}")
    public ResponseEntity<ApiResponse<?>> confirmPassword(
            @PathVariable("id") Long id, @Valid @RequestBody PasswordUpdateRequestDTO passwordUpdateRequestDTO) throws Exception {
        biodataService.confirmPassword(id, passwordUpdateRequestDTO);

        ApiResponse<?> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "Password telah terkonfirmasi", null);
        return ResponseEntity.ok(successResponse);
    }

    @PostMapping("/update-password/{id}")
    public ResponseEntity<ApiResponse<?>> updatePassword(
            @PathVariable Long id, @Valid @RequestBody PasswordUpdateRequestDTO passwordUpdateRequestDTO) throws Exception {
        biodataService.updatePassword(id, passwordUpdateRequestDTO);

        ApiResponse<?> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "Password telah terupdate", null);
        return ResponseEntity.ok(successResponse);
    }
}
