package com._a.backend.services.impl;

import com._a.backend.dtos.requests.PaymentMethodRequestDTO;
import com._a.backend.dtos.responses.PaymentMethodResponseDTO;
import com._a.backend.entities.PaymentMethod;
import com._a.backend.repositories.PaymentMethodRepository;
import com._a.backend.services.Services;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentMethodServiceImpl implements Services<PaymentMethodRequestDTO, PaymentMethodResponseDTO> {
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Page<PaymentMethodResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<PaymentMethod> paymentMethods = paymentMethodRepository.findAllByIsDeleteFalse(pageable);
        Page<PaymentMethodResponseDTO> paymentMethodResponseDTOS =
                paymentMethods.map(paymentMethod -> modelMapper.map(paymentMethod, PaymentMethodResponseDTO.class));
        return paymentMethodResponseDTOS;
    }

    public Page<PaymentMethodResponseDTO> getByName(int pageNo, int pageSize, String sortBy, String sortDirection, String name) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<PaymentMethod> paymentMethods = paymentMethodRepository.findByNameContainingIgnoreCaseAndIsDeleteFalse(pageable, name);
        Page<PaymentMethodResponseDTO> paymentMethodResponseDTOS =
                paymentMethods.map(paymentMethod -> modelMapper.map(paymentMethod, PaymentMethodResponseDTO.class));
        return paymentMethodResponseDTOS;
    }

    @Override
    public List<PaymentMethodResponseDTO> findAll() {
        return List.of();
    }

    @Override
    public Optional<PaymentMethodResponseDTO> findById(Long id) {
        Optional<PaymentMethod> paymentMethod = paymentMethodRepository.findById(id);
        if (paymentMethod.isPresent()) {
            Optional<PaymentMethodResponseDTO> paymentMethodResponseDTO = paymentMethod.map(
                    paymentMethodOptional -> modelMapper.map(paymentMethodOptional, PaymentMethodResponseDTO.class));
            return paymentMethodResponseDTO;
        }

        return Optional.empty();
    }

    @Override
    public PaymentMethodResponseDTO save(PaymentMethodRequestDTO paymentMethodRequestDTO) {
        if (paymentMethodRepository.existsByNameContainingIgnoreCaseAndIsDeleteFalse(paymentMethodRequestDTO.getName())){
            throw new IllegalArgumentException("Name already exists");
        }

        PaymentMethod paymentMethod = paymentMethodRepository.save(modelMapper.map(paymentMethodRequestDTO, PaymentMethod.class));
        PaymentMethodResponseDTO paymentMethodResponseDTO = modelMapper.map(paymentMethod, PaymentMethodResponseDTO.class);
        return paymentMethodResponseDTO;
    }

    @Override
    public PaymentMethodResponseDTO update(PaymentMethodRequestDTO paymentMethodRequestDTO, Long id) {
        Optional<PaymentMethod> optionalPaymentMethod = paymentMethodRepository.findById(id);
        if (paymentMethodRepository.existsByNameContainingIgnoreCaseAndIsDeleteFalseAndIdNot(paymentMethodRequestDTO.getName(), id)) {
            throw new IllegalArgumentException("Name already exists");
        } else if (optionalPaymentMethod.isPresent()) {
            PaymentMethod paymentMethod = optionalPaymentMethod.get();

            modelMapper.map(paymentMethodRequestDTO, paymentMethod);

            PaymentMethod updatedPaymentMethod = paymentMethodRepository.save(paymentMethod);

            return modelMapper.map(updatedPaymentMethod, PaymentMethodResponseDTO.class);
        }
        throw new IllegalArgumentException("Payment Method not found");
    }

    public void softDeletePaymentMethod(Long id) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment Method not found"));

        boolean hasActiveMedicalItemPurchase = paymentMethod.getMedicalItemPurchases().stream()
                .anyMatch(medicalItemPurchase -> !medicalItemPurchase.getIsDelete());

        if (hasActiveMedicalItemPurchase) {
            throw new IllegalStateException("Cannot delete payment method: it's active on medical item purchase.");
        }

        paymentMethod.setIsDelete(true);
//        paymentMethod.setDeletedBy(userId);
        paymentMethod.setDeletedOn(LocalDateTime.now());
        paymentMethodRepository.save(paymentMethod);
    }

    @Override
    public void deleteById(Long id) {
        paymentMethodRepository.deleteById(id);
    }
}
