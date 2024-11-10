package com._a.backend.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.responses.CustomerMemberSummaryResponseDTO;
import com._a.backend.dtos.responses.CustomerSummaryResponseDTO;
import com._a.backend.entities.Customer;
import com._a.backend.entities.User;
import com._a.backend.repositories.CustomerMemberRepository;
import com._a.backend.repositories.CustomerRepository;
import com._a.backend.services.AuthService;
import com._a.backend.services.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CustomerMemberRepository customerMemberRepository;

  @Autowired
  private AuthService authService;

  @Override
  public Customer getByUserId(Long userId) {
    return customerRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("Customer with user id: " + userId + " not found"));
  }

  @Override
  public CustomerSummaryResponseDTO getCustomerMembers() {
    User user = authService.getDetails();
    Long biodataId = user.getBiodataId();
    List<CustomerMemberSummaryResponseDTO> customerMembers = customerMemberRepository
        .findCustomerMembersByUserBiodataId(biodataId);

    CustomerSummaryResponseDTO responseDTO = new CustomerSummaryResponseDTO();
    responseDTO.setCustomerMemberList(customerMembers);
    responseDTO.setUserId(user.getId());

    return responseDTO;
  }

}
