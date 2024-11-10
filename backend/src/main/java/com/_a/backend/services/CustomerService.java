package com._a.backend.services;

import com._a.backend.dtos.responses.CustomerSummaryResponseDTO;
import com._a.backend.entities.Customer;

public interface CustomerService {
  Customer getByUserId(Long userId);

  CustomerSummaryResponseDTO getCustomerMembers();
}
