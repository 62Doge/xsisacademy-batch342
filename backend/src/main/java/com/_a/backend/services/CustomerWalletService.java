package com._a.backend.services;

import com._a.backend.dtos.responses.CustomerWalletSummaryResponseDto;

public interface CustomerWalletService {
  CustomerWalletSummaryResponseDto getBalanceByUserId(Long userid);
}
