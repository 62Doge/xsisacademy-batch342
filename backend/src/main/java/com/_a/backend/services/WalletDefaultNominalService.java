package com._a.backend.services;

import java.util.List;

import com._a.backend.dtos.projections.WalletDefaultNominalProjectionDto;
import com._a.backend.entities.WalletDefaultNominal;

public interface WalletDefaultNominalService {
  List<WalletDefaultNominalProjectionDto> getNominalLessThanEqualBalance();

  WalletDefaultNominal getById(Long id);
}
