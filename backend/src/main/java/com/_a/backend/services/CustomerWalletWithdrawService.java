package com._a.backend.services;

import com._a.backend.dtos.requests.CustomerWalletWithdrawRequestDto;
import com._a.backend.dtos.responses.CustomerWalletWithdrawOtpResponseDto;

public interface CustomerWalletWithdrawService {
  CustomerWalletWithdrawOtpResponseDto create(CustomerWalletWithdrawRequestDto requestDto);
}
