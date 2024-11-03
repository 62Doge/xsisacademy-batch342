package com._a.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.CustomerWalletWithdrawRequestDto;
import com._a.backend.dtos.responses.CustomerWalletWithdrawOtpResponseDto;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.CustomerWalletWithdrawService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/patient/wallet-withdraw")
public class CustomerWalletWithdrawController {
  @Autowired
  private CustomerWalletWithdrawService withdrawService;

  @PostMapping({ "", "/" })
  public ResponseEntity<ApiResponse<CustomerWalletWithdrawOtpResponseDto>> withdraw(
      @RequestBody CustomerWalletWithdrawRequestDto requestDto) {
    System.out.println("===== requestDto =====");
    System.out.println(requestDto);
    CustomerWalletWithdrawOtpResponseDto responseDto = withdrawService.create(requestDto);
    return new ResponseEntity<>(ApiResponse.success(201, responseDto), HttpStatus.CREATED);
  }

}
