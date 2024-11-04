package com._a.backend.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._a.backend.dtos.requests.ForgotPasswordRequestDto;
import com._a.backend.dtos.requests.ResetPasswordRequestDTO;
import com._a.backend.dtos.requests.VerifyOtpRequestDto;
import com._a.backend.entities.ResetPassword;
import com._a.backend.entities.Token;
import com._a.backend.entities.User;
import com._a.backend.exceptions.InvalidPasswordException;
import com._a.backend.exceptions.InvalidTokenException;
import com._a.backend.exceptions.NewPasswordConfirmationException;
import com._a.backend.exceptions.UserNotFoundException;
import com._a.backend.repositories.ResetPasswordRepository;
import com._a.backend.repositories.TokenRepository;
import com._a.backend.repositories.UserRepository;
import com._a.backend.services.EmailService;
import com._a.backend.services.ResetPasswordService;

@Service
public class PasswordResetServiceImpl implements ResetPasswordService {
  @Autowired
  UserRepository userRepository;

  @Autowired
  private TokenRepository tokenRepository;

  @Autowired
  private ResetPasswordRepository resetPasswordRepository;

  @Autowired
  private EmailService emailService;

  @Override
  public void requestPasswordReset(ForgotPasswordRequestDto requestDto) throws Exception {
    User user = userRepository.findByEmail(requestDto.getEmail())
        .orElseThrow(() -> new UserNotFoundException("Email tidak terdaftar"));

    tokenRepository.findActiveTokenByEmail(user.getEmail(), LocalDateTime.now())
        .ifPresent(token -> {
          token.setIsExpired(true);
          tokenRepository.save(token);
        });

    emailService.sendOtp(user);
  }

  @Override
  public void verifyOtp(VerifyOtpRequestDto requestDto) {
    Token token = tokenRepository
        .findActiveTokenByEmailAndToken(requestDto.getEmail(), requestDto.getOtp(), LocalDateTime.now()).orElse(null);

    if (token == null) {
      if (tokenRepository.existsExpiredTokenByEmailAndToken(requestDto.getEmail(), requestDto.getOtp(),
          LocalDateTime.now())) {
        throw new InvalidTokenException("Kode OTP kadaluarsa");
      } else {
        throw new InvalidTokenException("OTP salah");
      }
    }

  }

  @Transactional
  @Override
  public void resetPassword(ResetPasswordRequestDTO requestDTO) throws Exception {
    String email = requestDTO.getEmail();
    String newPassword = requestDTO.getNewPassword();
    String confirmPassword = requestDTO.getConfirmPassword();
    String otp = requestDTO.getOtp();
    VerifyOtpRequestDto verifyOtpRequestDto = new VerifyOtpRequestDto(email, otp);
    verifyOtp(verifyOtpRequestDto);

    if (!newPassword.equals(confirmPassword)) {
      throw new NewPasswordConfirmationException("Password tidak sama dengan konfirrmasi password");
    }

    if (!isValidPassword(newPassword)) {
      throw new InvalidPasswordException(
          "password tidak memenuhi standar (minimal 8 karakter, harus mengandung huruf besar, huruf kecil, angka, dan special character)");
    }

    User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("Email tidak terdaftar"));

    ResetPassword resetPassword = new ResetPassword();
    resetPassword.setOldPassword(user.getPassword());
    resetPassword.setNewPassword(newPassword);
    resetPassword.setResetFor("RESET_PASSWORD");
    resetPasswordRepository.save(resetPassword);

    user.setPassword(newPassword);
    userRepository.save(user);
  }

  private boolean isValidPassword(String password) {
    return password.length() >= 8 &&
        password.matches(".*[A-Z].*") &&
        password.matches(".*[a-z].*") &&
        password.matches(".*\\d.*") &&
        password.matches(".*\\W.*");
  }
}
