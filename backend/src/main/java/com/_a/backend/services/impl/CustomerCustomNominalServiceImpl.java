package com._a.backend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.projections.CustomerCustomNominalProjectionDto;
import com._a.backend.entities.CustomerCustomNominal;
import com._a.backend.repositories.CustomerCustomNominalRepository;
import com._a.backend.services.AuthService;
import com._a.backend.services.CustomerCustomNominalService;
import com._a.backend.services.CustomerService;
import com._a.backend.services.CustomerWalletService;

@Service
public class CustomerCustomNominalServiceImpl implements CustomerCustomNominalService {
  @Autowired
  private CustomerCustomNominalRepository customerCustomNominalRepository;

  @Autowired
  AuthService authService;

  @Autowired
  CustomerWalletService customerWalletService;

  @Autowired
  CustomerService customerService;

  @Override
  public CustomerCustomNominal create(int nominal) {
    Double balance = customerWalletService.getBalance();

    if (nominal > balance) {
      throw new IllegalArgumentException("Nominal can't greather than balance!");
    }
    Long userId = authService.getDetails().getId();
    CustomerCustomNominal customNominal = new CustomerCustomNominal();
    customNominal.setNominal(nominal);
    customNominal.setCustomerId(customerService.getByUserId(userId).getId());
    customNominal.setCreatedBy(userId);
    return customerCustomNominalRepository.save(customNominal);
  }

  @Override
  public List<CustomerCustomNominalProjectionDto> getNominalLessThanEqualBalance() {
    Double balance = customerWalletService.getBalance();
    Long userId = authService.getDetails().getId();

    return customerCustomNominalRepository.findAllByNominalLessThanEqual(balance, userId).orElse(null);
  }

  @Override
  public CustomerCustomNominal getById(Long id) {
    return customerCustomNominalRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Custom nominal with id: " + id + " not found"));
  }

}
