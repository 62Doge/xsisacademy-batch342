package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class CustomerWalletWithdrawOtpResponseDto {
  private String otp;
  private String expiredIn = "1 Jam";
}
