package com._a.backend.services;

import com._a.backend.dtos.requests.ForgotPasswordRequestDto;

public interface ResetPasswordService {
  void requestPasswordReset(ForgotPasswordRequestDto requestDto) throws Exception;

  void verifyOtp(String email, String otp);

  void resetPassword(String email, String otp, String newPassword, String confirmPassword) throws Exception;
}
