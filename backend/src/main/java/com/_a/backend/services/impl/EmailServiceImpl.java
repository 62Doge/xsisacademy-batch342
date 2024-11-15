package com._a.backend.services.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com._a.backend.entities.Token;
import com._a.backend.entities.User;
import com._a.backend.repositories.TokenRepository;
import com._a.backend.services.EmailService;
import com._a.backend.utils.OtpGenerator;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private TemplateEngine templateEngine;

  @Autowired
  private TokenRepository tokenRepository;

  private final String FROM_NOREPLY = "noreply@halodog.com";

  @Override
  public void sendOtp(User user) throws MessagingException {
    String otp = OtpGenerator.generate();

    Token token = new Token();
    token.setEmail(user.getEmail());
    token.setUserId(user
        .getId());
    token.setToken(otp);
    token.setExpiredOn(LocalDateTime.now().plusMinutes(10));
    token.setIsExpired(false);
    token.setUsed_for("RESET_PASSWORD");
    token.setCreatedBy(user.getId());
    tokenRepository.save(token);

    Context context = new Context();
    context.setVariable("otp", otp);
    context.setVariable("name", user.getBiodata().getFullname());

    String htmlContent = templateEngine.process("emails/resetPassword", context);

    sendHtmlEmail(FROM_NOREPLY, user.getEmail(), "Permintaan Reset Password", htmlContent);
  }

  public void sendOtpRegistration(String email, String otp) throws MessagingException {
    Context context = new Context();
    context.setVariable("otp", otp);
    context.setVariable("email", email);

    String htmlContent = templateEngine.process("emails/registrationOtp", context);

    sendHtmlEmail(FROM_NOREPLY, email, "Account Registration OTP", htmlContent);
  }

  public void sendOtpUpdateEmail(String email, String otp) throws MessagingException {
    Context context = new Context();
    context.setVariable("otp", otp);
    context.setVariable("email", email);

    String htmlContent = templateEngine.process("emails/updateEmailOtp", context);

    sendHtmlEmail(FROM_NOREPLY, email, "Update Email OTP", htmlContent);
  }

  @Override
  public void sendHtmlEmail(String from, String to, String subject, String htmlContent) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true);
    helper.setFrom(from);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlContent, true);

    mailSender.send(message);
  }

}
