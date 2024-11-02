package com._a.backend.services;

import java.util.List;

import com._a.backend.dtos.projections.WalletDefaultNominalProjectionDto;

public interface WalletDefaultNominalService {
  List<WalletDefaultNominalProjectionDto> getNominalLessThanEqualBalance();
}
