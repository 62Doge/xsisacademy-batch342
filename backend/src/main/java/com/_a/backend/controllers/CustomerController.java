package com._a.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.responses.CustomerSummaryResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.CustomerService;

@RestController
@RequestMapping("api/customer")
@CrossOrigin("*")
public class CustomerController {
  @Autowired
  private CustomerService customerService;

  @GetMapping({ "members", "members/" })
  public ResponseEntity<ApiResponse<CustomerSummaryResponseDTO>> getCustomerMembers() {
    return new ResponseEntity<>(
        ApiResponse.success(200, customerService.getCustomerMembers()),
        HttpStatus.OK);
  }
}
