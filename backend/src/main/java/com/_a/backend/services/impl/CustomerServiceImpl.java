package com._a.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.entities.Customer;
import com._a.backend.repositories.CustomerRepository;
import com._a.backend.services.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
  @Autowired
  private CustomerRepository customerRepository;

  @Override
  public Customer getByUserId(Long userId) {
    return customerRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("Customer with user id: " + userId + " not found"));
  }

}
