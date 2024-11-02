package com._a.backend.dtos.responses;

import java.time.LocalDateTime;

import com._a.backend.entities.CustomerWallet;

import lombok.Getter;

@Getter
public class CustomerWalletSummaryResponseDto {
  private Long id;
  private Double balance;
  private Double points;
  private int pinAttempt;
  private LocalDateTime blockEnds;
  private boolean isBlocked;

  public CustomerWalletSummaryResponseDto(CustomerWallet customerWallet) {
    this.id = customerWallet.getId();
    this.balance = customerWallet.getBalance();
    this.points = customerWallet.getPoints();
    this.pinAttempt = customerWallet.getPinAttempt();
    this.blockEnds = customerWallet.getBlockEnds();
    this.isBlocked = customerWallet.isBlocked();
  }
}
