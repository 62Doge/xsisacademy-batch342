package com._a.backend.dtos.projections;

public interface WalletDefaultNominalProjectionDto {
  Long getId();

  int getNominal();

  default String getType() {
    return "default";
  }
}
