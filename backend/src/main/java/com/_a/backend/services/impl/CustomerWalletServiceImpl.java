package com._a.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.responses.CustomerWalletSummaryResponseDto;
import com._a.backend.repositories.CustomerWalletRepository;
import com._a.backend.services.CustomerWalletService;

@Service
public class CustomerWalletServiceImpl implements CustomerWalletService {
  @Autowired
  private CustomerWalletRepository walletRepository;

  @Override
  public CustomerWalletSummaryResponseDto getBalanceByUserId(Long userid) {
    return walletRepository.findBalanceByUserId(userid).orElseThrow(
        () -> new RuntimeException("User with id: " + userid + " don't have wallet. Wallet just for patient role"));
  }

}
