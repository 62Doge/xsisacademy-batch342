package com._a.backend.services.impl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com._a.backend.dtos.requests.*;
import com._a.backend.dtos.responses.PersonalDataResponseDTO;
import com._a.backend.entities.*;
import com._a.backend.exceptions.InvalidPasswordException;
import com._a.backend.exceptions.InvalidTokenException;
import com._a.backend.exceptions.PasswordMismatchException;
import com._a.backend.exceptions.TokenRequestTooSoonException;
import com._a.backend.repositories.*;
import com._a.backend.utils.OtpGenerator;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com._a.backend.dtos.responses.BiodataResponseDTO;
import com._a.backend.services.Services;

import jakarta.transaction.Transactional;

@Service
public class BiodataServiceImpl implements Services<BiodataRequestDTO, BiodataResponseDTO> {

    @Autowired
    BiodataRepository biodataRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ResetPasswordRepository resetPasswordRepository;


    private final String uploadDirectory = "/assets/img/doctorphotos/";

    @Override
    public Page<BiodataResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Biodata> biodatas = biodataRepository.findAllByIsDeleteFalse(pageable);
        Page<BiodataResponseDTO> biodataResponseDTOS = biodatas
                .map(biodata -> modelMapper.map(biodata, BiodataResponseDTO.class));
        return biodataResponseDTOS;
    }

    @Override
    public void deleteById(Long id) {
        biodataRepository.deleteById(id);
    }

    @Override
    public List<BiodataResponseDTO> findAll() {
        // using less optimal approach, but easier to understand
        return biodataRepository.findAll().stream()
                .map(biodata -> modelMapper.map(biodata, BiodataResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BiodataResponseDTO> findById(Long id) {
        return biodataRepository.findById(id)
                .map(biodata -> modelMapper.map(biodata, BiodataResponseDTO.class));
    }

    @Override
    public BiodataResponseDTO save(BiodataRequestDTO biodataRequestDTO) {
        Biodata biodata = biodataRepository.save(modelMapper.map(biodataRequestDTO, Biodata.class));
        return modelMapper.map(biodata, BiodataResponseDTO.class);
    }

    @Transactional
    @Override
    public BiodataResponseDTO update(BiodataRequestDTO biodataRequestDTO, Long id) {
        Biodata biodata = biodataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Biodata not found"));

        modelMapper.map(biodataRequestDTO, biodata);
        Biodata updatedBiodata = biodataRepository.save(biodata);

        return modelMapper.map(updatedBiodata, BiodataResponseDTO.class);
    }

    public BiodataResponseDTO uploadImage(Long biodataId, MultipartFile image) {
        try {
            // Menentukan direktori untuk menyimpan gambar
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Menyimpan file ke direktori
            String fileName = image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), filePath);

            // Menyimpan path gambar ke dalam entity Biodata
            Optional<Biodata> optionalBiodata = biodataRepository.findById(biodataId);
            if (optionalBiodata.isPresent()) {
                Biodata biodata = optionalBiodata.get();
                biodata.setImagePath(uploadDirectory + fileName);
                biodataRepository.save(biodata);
                return modelMapper.map(biodata, BiodataResponseDTO.class);
            } else {
                throw new RuntimeException("Biodata not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public PersonalDataResponseDTO getPersonalData(Long id) {
        Biodata biodata = biodataRepository.findById(id).orElseThrow(() -> new RuntimeException("Biodata not found"));
        PersonalDataResponseDTO personalDataResponseDTO = new PersonalDataResponseDTO();
        personalDataResponseDTO.setId(biodata.getId());
        personalDataResponseDTO.setFullName(biodata.getFullname());
        personalDataResponseDTO.setMobilePhone(biodata.getMobilePhone());

        Customer customer = customerRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        personalDataResponseDTO.setDob(customer.getDob());

        return  personalDataResponseDTO;
    }

    public PersonalDataResponseDTO updatePersonalData(Long id, PersonalDataRequestDTO personalDataRequestDTO){
        Biodata biodata = biodataRepository.findById(id).orElseThrow(() -> new RuntimeException("Biodata not found"));
        biodata.setFullname(personalDataRequestDTO.getFullName());
        biodata.setMobilePhone("+62" + personalDataRequestDTO.getMobilePhone());

        Customer customer = customerRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        if (personalDataRequestDTO.getDob().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Tidak bisa melewati hari ini");
        }
        customer.setDob(personalDataRequestDTO.getDob());

        biodata = biodataRepository.save(biodata);
        customer = customerRepository.save(customer);

        //convert
        PersonalDataResponseDTO personalDataResponseDTO = new PersonalDataResponseDTO();
        personalDataResponseDTO.setId(biodata.getId());
        personalDataResponseDTO.setFullName(biodata.getFullname());
        personalDataResponseDTO.setMobilePhone(biodata.getMobilePhone());
        personalDataResponseDTO.setDob(customer.getDob());

        return personalDataResponseDTO;
    }

    public BiodataResponseDTO getProfileData(Long id) {
        Biodata biodata = biodataRepository.findById(id).orElseThrow(() -> new RuntimeException("Biodata not found"));
        BiodataResponseDTO biodataResponseDTO = new BiodataResponseDTO();
        biodataResponseDTO.setId(biodata.getId());
        biodataResponseDTO.setFullName(biodata.getFullname());
        biodataResponseDTO.setMobilePhone(biodata.getMobilePhone());
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        biodataResponseDTO.setEmail(user.getEmail());
        Customer customer = customerRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        biodataResponseDTO.setDob(customer.getDob());
        return biodataResponseDTO;
    }

    public void sendOtpForEmailUpdate(EmailRequestDTO emailRequestDTO) throws Exception {
        if (userRepository.existsByEmailContainingIgnoreCase(emailRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email sudah terdaftar");
        }

        String otp = OtpGenerator.generate();

        Token activeToken = tokenRepository.findActiveTokenByEmail(emailRequestDTO.getEmail(), LocalDateTime.now())
                .orElse(null);

        if (activeToken != null) {
            LocalDateTime createdOn = activeToken.getCreatedOn();
            if (createdOn != null && LocalDateTime.now().isBefore(createdOn.plusMinutes(1))) {
                Duration duration = Duration.between(LocalDateTime.now(), createdOn.plusMinutes(1));
                long minutes = duration.toMinutes();
                long seconds = duration.minusMinutes(minutes).toSeconds();

                throw new TokenRequestTooSoonException(
                        "Anda harus menunggu sebelum meminta token baru. Silakan coba lagi setelah "
                                + minutes + ":" + seconds + " (menit:detik)");
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
        newToken.setUsed_for("UPDATE_EMAIL");
        tokenRepository.save(newToken);

        try {
            emailService.sendOtpUpdateEmail(emailRequestDTO.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public void verifyOtp(Long id,VerifyOtpRequestDto requestDto) throws Exception{
        Token token = tokenRepository
                .findActiveTokenByEmailAndToken(requestDto.getEmail(), requestDto.getOtp(), LocalDateTime.now()).orElse(null);

        if (token == null) {
            if (tokenRepository.existsExpiredTokenByEmailAndToken(requestDto.getEmail(), requestDto.getOtp(), LocalDateTime.now())) {
                throw new InvalidTokenException("Kode OTP kadaluarsa.");
            } else {
                throw new InvalidTokenException("Kode OTP salah.");
            }
        }
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(requestDto.getEmail());
        userRepository.save(user);
    }


    public void confirmPassword(Long id, PasswordUpdateRequestDTO passwordUpdateRequestDTO) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(passwordUpdateRequestDTO.getPassword(), user.getPassword())) {
            throw new PasswordMismatchException("Password tidak sama dengan password akun anda");
        }

        if (!passwordUpdateRequestDTO.getConfirmPassword().equals(passwordUpdateRequestDTO.getPassword())) {
            throw new PasswordMismatchException("Password tidak sama dengan konfirmasi password");
        }
    }

    public void updatePassword(Long id, PasswordUpdateRequestDTO passwordUpdateRequestDTO) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordUpdateRequestDTO.getConfirmPassword().equals(passwordUpdateRequestDTO.getPassword())) {
            throw new PasswordMismatchException("Password tidak sama dengan konfirmasi password");
        }
        if (!isValidPassword(passwordUpdateRequestDTO.getPassword())) {
            throw new InvalidPasswordException("Password tidak memenuhi standar (minimal 8 karakter, harus mengandung huruf besar, huruf kecil, angka, dan special character)");
        }
        ResetPassword resetPassword = new ResetPassword();
        resetPassword.setOldPassword(passwordEncoder.encode(user.getPassword()));
        resetPassword.setNewPassword(passwordEncoder.encode(passwordUpdateRequestDTO.getPassword()));
        resetPassword.setResetFor("UPDATE_PASSWORD");
        resetPassword.setCreatedBy(user.getId());
        resetPasswordRepository.save(resetPassword);

        user.setPassword(passwordEncoder.encode(passwordUpdateRequestDTO.getPassword()));
        userRepository.save(user);
    }

    public boolean hasActiveToken(String email) {
        Token activeToken = tokenRepository.findActiveTokenByEmail(email, LocalDateTime.now()).orElse(null);
        return activeToken != null;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*\\W.*");
    }

}
