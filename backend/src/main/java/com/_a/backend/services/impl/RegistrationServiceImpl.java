package com._a.backend.services.impl;

import com._a.backend.dtos.requests.EmailRequestDTO;
import com._a.backend.dtos.requests.RegistrationRequestDTO;
import com._a.backend.dtos.requests.VerifyOtpRequestDto;
import com._a.backend.entities.*;
import com._a.backend.exceptions.*;
import com._a.backend.repositories.*;
import com._a.backend.utils.OtpGenerator;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.Duration;
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
    @Autowired
    private PasswordResetServiceImpl passwordResetService;
    @Autowired
    private BiodataRepository biodataRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CustomerRepository customerRepository;


    public void registration(RegistrationRequestDTO registrationRequestDTO) {
        Biodata biodata = new Biodata();
        biodata.setFullname(registrationRequestDTO.getFullName());
        biodata.setMobilePhone(registrationRequestDTO.getMobilePhone());
        biodataRepository.save(biodata);

        User user = new User();
        user.setEmail(registrationRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequestDTO.getPassword()));
        user.setRoleId(registrationRequestDTO.getRoleId());
        user.setBiodataId(biodata.getId());
        userRepository.save(user);

        // set code and str for doctor
        setCode(registrationRequestDTO.getRoleId(), biodata);
    }

    public void sendOtp(EmailRequestDTO emailRequestDTO) throws Exception {
        if (checkEmail(emailRequestDTO)) {
            throw new IllegalArgumentException("Email already exists");
        }

        String otp = OtpGenerator.generate();
        Token activeToken = tokenRepository.findActiveTokenByEmail(emailRequestDTO.getEmail(), LocalDateTime.now())
                .orElse(null);

        if (activeToken != null) {
            LocalDateTime createdOn = activeToken.getCreatedOn();
            if (createdOn != null && LocalDateTime.now().isBefore(createdOn.plusMinutes(3))) {
                Duration duration = Duration.between(LocalDateTime.now(), createdOn.plusMinutes(3));
                long minutes = duration.toMinutes();
                long seconds = duration.minusMinutes(minutes).toSeconds();

                throw new TokenRequestTooSoonException(
                        "You need to wait before requesting a new token. Please try again after "
                                + minutes + ":" + seconds + " (minutes:seconds)");
            }

            activeToken.setIsExpired(true);
            tokenRepository.save(activeToken);
        }

        Token newToken = new Token();
        newToken.setCreatedOn(LocalDateTime.now());
        newToken.setEmail(emailRequestDTO.getEmail());
        newToken.setToken(otp);
        newToken.setExpiredOn(LocalDateTime.now().plusMinutes(10));
        newToken.setIsExpired(false);
        newToken.setUsed_for("REGISTRATION_ACCOUNT");
        tokenRepository.save(newToken);

        try {
            emailService.sendOtpRegistration(emailRequestDTO.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public void verifyOtp(VerifyOtpRequestDto requestDto) throws Exception{
        Token token = tokenRepository
                .findActiveTokenByEmailAndToken(requestDto.getEmail(), requestDto.getOtp(), LocalDateTime.now()).orElse(null);

        if (token == null) {
            if (isExpiredTokenExists(requestDto)) {
                throw new InvalidTokenException("Your OTP has expired");
            } else {
                throw new InvalidTokenException("Your OTP is incorrect");
            }
        }
    }

    public void confirmPassword(RegistrationRequestDTO registrationRequestDTO) throws Exception{
        if (!registrationRequestDTO.getPassword().equals(registrationRequestDTO.getConfirmPassword())) {
            throw new NewPasswordConfirmationException("Passwords do not match");
        }
        if (!isValidPassword(registrationRequestDTO.getPassword())) {
            throw new InvalidPasswordException("Password does not meet the standard (minimum 8 characters, " +
                    "must contain an uppercase letter, a lowercase letter, a number, and a special character).");
        }
    }

    public boolean isExpiredTokenExists(VerifyOtpRequestDto requestDto) {
        return tokenRepository.existsExpiredTokenByEmailAndToken(requestDto.getEmail(), requestDto.getOtp(), LocalDateTime.now());
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*\\W.*");
    }

    public boolean checkEmail(EmailRequestDTO emailRequestDTO) {
        return userRepository.existsByEmail(emailRequestDTO.getEmail());
    }

    private boolean isAdminRole(Long roleId) {
        return roleId.equals(1L);
    }

    private boolean isDoctorRole(Long roleId) {
        return roleId.equals(2L);
    }

    private boolean isCustomerRole(Long roleId) {
        return roleId.equals(3L);
    }

    private void setCode(Long id, Biodata biodata) {
        // Create Doctor with generated `str` and biodata ID
        if (isAdminRole(id)){
            Admin admin = new Admin();
            admin.setBiodataId(biodata.getId());
            admin.setCode(generateNextAdminCode());
            adminRepository.save(admin);
        } else if (isDoctorRole(id)) {
            Doctor doctor = new Doctor();
            doctor.setBiodataId(biodata.getId());
            doctor.setStr(generateNextDoctorStr());
            doctorRepository.save(doctor);
        }else if (isCustomerRole(id)) {
            Customer customer = new Customer();
            customer.setBiodataId(biodata.getId());
            customerRepository.save(customer);
        }
    }

    private String generateNextDoctorStr() {
        Doctor lastDoctor = doctorRepository.findTopByOrderByStrDesc();

        if (lastDoctor != null && lastDoctor.getStr() != null) {
            String lastStr = lastDoctor.getStr();
            int lastNumber = Integer.parseInt(lastStr.substring(4));
            int nextNumber = lastNumber + 1;

            DecimalFormat formatter = new DecimalFormat("000");
            return "DKTR" + formatter.format(nextNumber);
        } else {
            return "DKTR001";
        }
    }

    private String generateNextAdminCode() {
        Admin lastAdmin = adminRepository.findTopByOrderByCodeDesc();

        if (lastAdmin != null && lastAdmin.getCode() != null) {
            String lastStr = lastAdmin.getCode();
            int lastNumber = Integer.parseInt(lastStr.substring(4));
            int nextNumber = lastNumber + 1;

            DecimalFormat formatter = new DecimalFormat("000");
            return "ADMN" + formatter.format(nextNumber);
        } else {
            return "ADMN001";
        }
    }


}
