package com._a.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.CustomerWalletCheckPinRequestDto;
import com._a.backend.dtos.responses.CustomerWalletSummaryResponseDto;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.CustomerWalletService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/patient/customer-wallet")
public class CustomerWalletController {
  @Autowired
  CustomerWalletService customerWalletService;

  @GetMapping({ "", "/" })
  public ResponseEntity<ApiResponse<CustomerWalletSummaryResponseDto>> getCustomerWallet() {
    CustomerWalletSummaryResponseDto responseDto = customerWalletService
        .getCustomerWallet();
    return new ResponseEntity<>(ApiResponse.success(200, responseDto), HttpStatus.OK);
  }

  @PostMapping({ "/check-pin", "/check-pin/" })
  public ResponseEntity<ApiResponse<Void>> postMethodName(@RequestBody CustomerWalletCheckPinRequestDto requestDto) {
    ApiResponse<Void> response = customerWalletService.checkPin(requestDto);
    HttpStatus status = HttpStatus.OK;
    if (response.getStatus() == 403) {
      status = HttpStatus.FORBIDDEN;
    } else if (response.getStatus() == 400) {
      status = HttpStatus.BAD_REQUEST;
    } else if (response.getStatus() == 404) {
      status = HttpStatus.NOT_FOUND;
    }
    return new ResponseEntity<>(response, status);
  }

}
