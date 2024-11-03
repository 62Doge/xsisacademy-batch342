package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class WalletNominalRequestDto {
  private String type;
  private Long id;
  int amount = 0;
}
