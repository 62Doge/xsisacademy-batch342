package com._a.backend.services;

import com._a.backend.entities.User;

import jakarta.mail.MessagingException;

public interface EmailService {
  void sendOtp(User user) throws MessagingException;

  void sendHtmlEmail(String from, String to, String subject, String htmlContent) throws MessagingException;
}
