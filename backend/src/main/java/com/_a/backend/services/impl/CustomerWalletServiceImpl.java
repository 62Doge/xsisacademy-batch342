package com._a.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.responses.CustomerWalletSummaryResponseDto;
import com._a.backend.repositories.CustomerWalletRepository;
import com._a.backend.services.AuthService;
import com._a.backend.services.CustomerWalletService;

@Service
public class CustomerWalletServiceImpl implements CustomerWalletService {
  @Autowired
  private CustomerWalletRepository walletRepository;

  @Autowired
  AuthService authService;

  @Override
  public CustomerWalletSummaryResponseDto getCustomerWallet() {
    Long userId = authService.getDetails().getId();

    return walletRepository.findByUserId(userId).orElseThrow(
        () -> new RuntimeException("User with id: " + userId + " don't have wallet. Wallet just for patient role"));
  }

  @Override
  public Double getBalance() {
    Long userId = authService.getDetails().getId();

    return walletRepository.findBalanceByUserId(userId).orElseThrow(
        () -> new RuntimeException("User with id: " + userId + " don't have wallet. Wallet just for patient role"));
  }

}
