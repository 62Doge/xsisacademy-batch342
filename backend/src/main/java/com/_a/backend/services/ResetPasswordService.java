package com._a.backend.services;

import com._a.backend.dtos.requests.ForgotPasswordRequestDto;
import com._a.backend.dtos.requests.VerifyOtpRequestDto;

public interface ResetPasswordService {
  void requestPasswordReset(ForgotPasswordRequestDto requestDto) throws Exception;

  void verifyOtp(VerifyOtpRequestDto requestDto);

  void resetPassword(String email, String otp, String newPassword, String confirmPassword) throws Exception;
}
