package com._a.backend.services;

import com._a.backend.dtos.requests.CustomerWalletCheckPinRequestDto;
import com._a.backend.dtos.responses.CustomerWalletSummaryResponseDto;
import com._a.backend.payloads.ApiResponse;

public interface CustomerWalletService {
  CustomerWalletSummaryResponseDto getCustomerWallet();

  Double getBalance();

  ApiResponse<Void> checkPin(CustomerWalletCheckPinRequestDto requestDto);

  void updateBalance(Double amount);
}
