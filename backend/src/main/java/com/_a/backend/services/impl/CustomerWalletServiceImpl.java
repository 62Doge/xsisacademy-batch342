package com._a.backend.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.CustomerWalletCheckPinRequestDto;
import com._a.backend.dtos.responses.CustomerWalletSummaryResponseDto;
import com._a.backend.entities.CustomerWallet;
import com._a.backend.exceptions.InvalidUpdateException;
import com._a.backend.exceptions.UserNotFoundException;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.CustomerWalletRepository;
import com._a.backend.services.AuthService;
import com._a.backend.services.CustomerWalletService;

@Service
public class CustomerWalletServiceImpl implements CustomerWalletService {
  private static final int MAX_PIN_ATTEMPTS = 3;

  @Autowired
  private CustomerWalletRepository walletRepository;

  @Autowired
  AuthService authService;

  @Override
  public CustomerWalletSummaryResponseDto getCustomerWallet() {
    Long userId = authService.getDetails().getId();

    return walletRepository.findSummaryByUserId(userId).orElseThrow(
        () -> new UserNotFoundException(
            "User with id: " + userId + " don't have wallet. Wallet just for patient role"));
  }

  @Override
  public Double getBalance() {
    Long userId = authService.getDetails().getId();

    return walletRepository.findBalanceByUserId(userId).orElseThrow(
        () -> new UserNotFoundException(
            "User with id: " + userId + " don't have wallet. Wallet just for patient role"));
  }

  @Override
  public ApiResponse<Void> checkPin(CustomerWalletCheckPinRequestDto requestDto) {
    Long userId = authService.getDetails().getId();
    String pin = walletRepository.findPinByUserId(userId)
        .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found!"));

    CustomerWalletSummaryResponseDto walletSummaryResponseDto = walletRepository.findSummaryByUserId(userId)
        .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " not found!"));

    if (walletSummaryResponseDto.isBlocked()) {
      return new ApiResponse<Void>(403, "Akun Anda diblokir karena terlalu banyak percobaan PIN yang salah.", null);
    }

    if (pin.equals(requestDto.getPin())) {
      walletRepository.resetPinAttempt(walletSummaryResponseDto.getId());

      return new ApiResponse<Void>(200, "Proses authentikasi berhasil", null);
    }

    walletRepository.incrementPinAttemptByUserId(walletSummaryResponseDto.getId());
    if (walletSummaryResponseDto.getPinAttempt() + 1 >= MAX_PIN_ATTEMPTS) {
      walletRepository.blockWallet(walletSummaryResponseDto.getId(), LocalDateTime.now());
      return new ApiResponse<Void>(403, "Akun anda diblokir karena melakukan banyak percobaan dengan PIN yang salah",
          null);
    }

    int attemptsChance = MAX_PIN_ATTEMPTS - (walletSummaryResponseDto.getPinAttempt() + 1);
    return new ApiResponse<Void>(400,
        "PIN salah. Anda memiliki " + attemptsChance + " percobaan lagi sebelum akun diblokir.", null);
  }

  @Override
  public void updateBalance(Double newBalance) {
    if (newBalance < 0) {
      throw new InvalidUpdateException("Balance can't less than 0");
    }
    Long userId = authService.getDetails().getId();

    CustomerWallet wallet = walletRepository.findByUserId(userId).orElseThrow(
        () -> new UserNotFoundException(
            "User with id: " + userId + " don't have wallet. Wallet just for patient role"));
    wallet.setBalance(newBalance);
    wallet.setModifiedBy(userId);
    walletRepository.save(wallet);
  }

}
