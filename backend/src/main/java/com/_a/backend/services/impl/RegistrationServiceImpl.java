package com._a.backend.services.impl;

import com._a.backend.dtos.requests.RegistrationRequestDTO;
import com._a.backend.dtos.requests.VerifyOtpRequestDto;
import com._a.backend.entities.Token;
import com._a.backend.entities.User;
import com._a.backend.exceptions.InvalidTokenException;
import com._a.backend.exceptions.UserNotFoundException;
import com._a.backend.repositories.TokenRepository;
import com._a.backend.repositories.UserRepository;
import com._a.backend.services.EmailService;
import com._a.backend.utils.OtpGenerator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RegistrationServiceImpl {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    public void sendOtp(RegistrationRequestDTO registrationRequestDTO) {
        if (checkEmail(registrationRequestDTO)) {
            throw new IllegalArgumentException("Email already exists");
        }
        String otp = OtpGenerator.generate();

        Token activeToken = new Token();
        activeToken.setCreatedOn(LocalDateTime.now());
        activeToken.setEmail(registrationRequestDTO.getEmail());
        activeToken.setToken(otp);
        activeToken.setExpiredOn(LocalDateTime.now().plusMinutes(10));
        activeToken.setIsExpired(false);
        activeToken.setUsed_for("REGISTRATION_ACCOUNT");
        tokenRepository.save(activeToken);

        try {
            emailService.sendOtpRegistration(registrationRequestDTO.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public boolean checkEmail(RegistrationRequestDTO registrationRequestDTO) {
        return tokenRepository.existsByEmail(registrationRequestDTO.getEmail());
    }

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
}
