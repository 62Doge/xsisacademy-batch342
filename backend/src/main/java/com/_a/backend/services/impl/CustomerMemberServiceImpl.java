package com._a.backend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.CustomerMemberRequestDTO;
import com._a.backend.dtos.responses.CustomerMemberResponseDTO;
import com._a.backend.entities.CustomerMember;
import com._a.backend.repositories.CustomerMemberRepository;
import com._a.backend.services.Services;

@Service
public class CustomerMemberServiceImpl implements Services<CustomerMemberRequestDTO, CustomerMemberResponseDTO> {

    @Autowired
    private CustomerMemberRepository customerMemberRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Page<CustomerMemberResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<CustomerMember> customerMembers = customerMemberRepository.findAllByIsDeleteFalse(pageable);
        Page<CustomerMemberResponseDTO> customerMemberDTOs = 
            customerMembers.map(customerMember -> modelMapper.map(customerMember, CustomerMemberResponseDTO.class));
        return customerMemberDTOs;
    }

    @Override
    public List<CustomerMemberResponseDTO> findAll() {
        List<CustomerMember> customerMembers = customerMemberRepository.findAll();
        List<CustomerMemberResponseDTO> customerMemberResponseDTOs = customerMembers.stream().map(
            customerMember -> modelMapper.map(customerMember, CustomerMemberResponseDTO.class)).toList();
        return customerMemberResponseDTOs;
    }

    @Override
    public Optional<CustomerMemberResponseDTO> findById(Long id) {
        Optional<CustomerMember> customerMember = customerMemberRepository.findById(id);
        if (customerMember.isPresent()) {
            Optional<CustomerMemberResponseDTO> customerMemberResponseDTO = 
                customerMember.map(customerMemberOptional -> modelMapper.map(customerMemberOptional, CustomerMemberResponseDTO.class));
                return customerMemberResponseDTO;
        }
        return Optional.empty();
    }

    @Override
    public CustomerMemberResponseDTO save(CustomerMemberRequestDTO requestDTO) {
        CustomerMember customerMember = customerMemberRepository.save(modelMapper.map(requestDTO, CustomerMember.class));
        CustomerMemberResponseDTO customerMemberResponseDTO = modelMapper.map(customerMember, CustomerMemberResponseDTO.class);
        return customerMemberResponseDTO;
    }

    @Override
    public CustomerMemberResponseDTO update(CustomerMemberRequestDTO requestDTO, Long id) {
        Optional<CustomerMember> optionalCustomerMember = customerMemberRepository.findById(id);
        if (optionalCustomerMember.isPresent()) {
            CustomerMember customerMember = optionalCustomerMember.get();
            modelMapper.map(requestDTO, customerMember);
            CustomerMember updatedCustomerMember = customerMemberRepository.save(customerMember);
            return modelMapper.map(updatedCustomerMember, CustomerMemberResponseDTO.class);
        }
        throw new RuntimeException("Customer Member not found");
    }

    @Override
    public void deleteById(Long id) {
        customerMemberRepository.deleteById(id);
    }

    public void softDeleteById(Long id) {
        Optional<CustomerMember> optionalCustomerMember = customerMemberRepository.findById(id);
        if (optionalCustomerMember.isPresent()) {
            CustomerMember customerMember = optionalCustomerMember.get();
            customerMember.setIsDelete(true);
            customerMember.setDeletedOn(LocalDateTime.now());
            customerMemberRepository.save(customerMember);
        } else {
            throw new RuntimeException("Customer Member not found");
        }
    }
    
}
