package com._a.backend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.projections.WalletDefaultNominalProjectionDto;
import com._a.backend.entities.WalletDefaultNominal;
import com._a.backend.repositories.WalletDefaultNominalRepository;
import com._a.backend.services.AuthService;
import com._a.backend.services.CustomerWalletService;
import com._a.backend.services.WalletDefaultNominalService;

@Service
public class WalletDefaultNominalServiceImpl implements WalletDefaultNominalService {
  @Autowired
  WalletDefaultNominalRepository defaultNominalRepository;

  @Autowired
  AuthService authService;

  @Autowired
  CustomerWalletService customerWalletService;

  @Override
  public List<WalletDefaultNominalProjectionDto> getNominalLessThanEqualBalance() {
    Long userId = authService.getDetails().getId();
    Double balance = customerWalletService.getBalance();

    return defaultNominalRepository.findAllByNominalLessThanEqual(balance, userId);
  }

  @Override
  public WalletDefaultNominal getById(Long id) {
    return defaultNominalRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Default nominal with id: " + id + " not found"));
  }

}
