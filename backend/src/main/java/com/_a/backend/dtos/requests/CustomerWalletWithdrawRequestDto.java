package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class CustomerWalletWithdrawRequestDto {
  private String pin;
  private WalletNominalRequestDto nominal;
}
