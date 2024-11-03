package com._a.backend.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._a.backend.dtos.requests.CustomerWalletCheckPinRequestDto;
import com._a.backend.dtos.requests.CustomerWalletWithdrawRequestDto;
import com._a.backend.dtos.responses.CustomerWalletWithdrawOtpResponseDto;
import com._a.backend.entities.CustomerCustomNominal;
import com._a.backend.entities.CustomerWalletWithdraw;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.CustomerWalletWithdrawRepository;
import com._a.backend.services.AuthService;
import com._a.backend.services.CustomerCustomNominalService;
import com._a.backend.services.CustomerService;
import com._a.backend.services.CustomerWalletService;
import com._a.backend.services.CustomerWalletWithdrawService;
import com._a.backend.services.WalletDefaultNominalService;
import com._a.backend.utils.OtpGenerator;

@Service
public class CustomerWalletWithdrawServiceImpl implements CustomerWalletWithdrawService {
  @Autowired
  private CustomerWalletWithdrawRepository customerWalletWithdrawRepository;

  @Autowired
  private CustomerWalletService customerWalletService;

  @Autowired
  private WalletDefaultNominalService walletDefaultNominalService;

  @Autowired
  private CustomerCustomNominalService customerCustomNominalService;

  @Autowired
  AuthService authService;

  @Autowired
  CustomerService customerService;

  @Transactional
  @Override
  public CustomerWalletWithdrawOtpResponseDto create(CustomerWalletWithdrawRequestDto requestDto) {
    CustomerWalletCheckPinRequestDto pinRequestDto = new CustomerWalletCheckPinRequestDto(requestDto.getPin());
    ApiResponse<Void> customerWalletCheckPin = customerWalletService.checkPin(pinRequestDto);
    if (customerWalletCheckPin.getStatus() != 200) {
      throw new RuntimeException(customerWalletCheckPin.getMessage());
    }
    Long walletDefaultNominalId = null;
    int amount = 0;
    if (requestDto.getNominal().getType().equals("default")) {
      walletDefaultNominalId = requestDto.getNominal().getId();
      amount = walletDefaultNominalService.getById(walletDefaultNominalId).getNominal();
    } else {
      if (requestDto.getNominal().getId() != null) {
        amount = customerCustomNominalService.getById(requestDto.getNominal().getId()).getNominal();
      } else {
        if (requestDto.getNominal().getAmount() > 0) {
          CustomerCustomNominal customNominal = customerCustomNominalService
              .create(requestDto.getNominal().getAmount());
          amount = customNominal.getNominal();
        } else {
          throw new RuntimeException("Amount should > 0, get: " + requestDto.getNominal().getAmount());
        }
      }
    }

    String otpString = OtpGenerator.generate();
    Long userId = authService.getDetails().getId();
    CustomerWalletWithdraw walletWithdraw = new CustomerWalletWithdraw();
    walletWithdraw.setCustomerId(customerService.getByUserId(userId).getId());
    walletWithdraw.setWalletDefaultNominalId(walletDefaultNominalId);
    walletWithdraw.setAmount(amount);
    walletWithdraw.setOtp(Integer.valueOf(otpString));
    walletWithdraw.setCreatedBy(userId);
    customerWalletWithdrawRepository.save(walletWithdraw);

    CustomerWalletWithdrawOtpResponseDto responseDto = new CustomerWalletWithdrawOtpResponseDto();
    responseDto.setOtp(otpString);
    return responseDto;
  }

}
