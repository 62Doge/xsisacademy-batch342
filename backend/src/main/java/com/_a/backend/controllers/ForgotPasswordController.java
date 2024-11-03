package com._a.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.ForgotPasswordRequestDto;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.ResetPasswordService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class ForgotPasswordController {
  @Autowired
  private ResetPasswordService resetPasswordService;

  @PostMapping({ "/forgot-password", "/forgot-password" })
  public ResponseEntity<ApiResponse<Void>> postMethodName(@RequestBody ForgotPasswordRequestDto requestDto)
      throws Exception {
    resetPasswordService.requestPasswordReset(requestDto);
    ApiResponse<Void> apiResponse = new ApiResponse<Void>(200, "OTP telah dikirim ke email anda", null);
    return new ResponseEntity<>(apiResponse, HttpStatus.OK);
  }

}
