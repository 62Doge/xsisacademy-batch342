package com._a.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.projections.WalletDefaultNominalProjectionDto;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.WalletDefaultNominalService;

import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/patient/wallet-default-nominal")
public class WalletDefaultNominalController {
  @Autowired
  WalletDefaultNominalService defaultNominalService;

  @GetMapping({ "/less-than-balance", "/less-than-balance/" })
  public ResponseEntity<ApiResponse<List<WalletDefaultNominalProjectionDto>>> getNominalsLessThanBalance() {
    List<WalletDefaultNominalProjectionDto> dtos = defaultNominalService.getNominalLessThanEqualBalance();
    return new ResponseEntity<>(ApiResponse.success(200, dtos), HttpStatus.OK);
  }

}
