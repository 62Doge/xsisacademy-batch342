package com._a.backend.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com._a.backend.dtos.requests.ForgotPasswordRequestDto;
import com._a.backend.entities.ResetPassword;
import com._a.backend.entities.User;
import com._a.backend.exceptions.TokenNotFoundException;
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

    emailService.sendOtp(user);
  }

  @Override
  public void verifyOtp(String email, String otp) {
    tokenRepository.findActiveTokenByEmailAndToken(email, otp, LocalDateTime.now())
        .orElseThrow(() -> new TokenNotFoundException("OTP salah"));

    if (tokenRepository.existsExpiredTokenByEmailAndToken(email, otp, LocalDateTime.now())) {
      throw new RuntimeException("Kode OTP kadaluarsa");
    }
  }

  @Transactional
  @Override
  public void resetPassword(String email, String otp, String newPassword, String confirmPassword) throws Exception {
    verifyOtp(email, otp);
    if (!newPassword.equals(confirmPassword)) {
      throw new Exception("Password tidak sama");
    }

    if (!isValidPassword(newPassword)) {
      throw new Exception("Password tidak memenuhi standar");
    }

    User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User tidak ditemukan"));

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
