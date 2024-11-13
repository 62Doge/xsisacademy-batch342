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

import com._a.backend.dtos.requests.BankRequestDTO;
import com._a.backend.dtos.responses.BankResponseDTO;
import com._a.backend.entities.Bank;
import com._a.backend.repositories.BankRepository;
import com._a.backend.services.Services;

@Service
public class BankServiceImpl implements Services<BankRequestDTO, BankResponseDTO> {

    @Autowired
    BankRepository bankRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DumpAuthServiceImpl dumpAuthServiceImpl;

    @Override
    public Page<BankResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Bank> banks = bankRepository.findAllByIsDeleteFalse(pageable);
        Page<BankResponseDTO> bankResponseDTOs = banks
                .map(bank -> modelMapper.map(bank, BankResponseDTO.class));
        return bankResponseDTOs;
    }

    public Page<Bank> findActivePages(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return bankRepository.findAllByIsDeleteFalse(pageable);
    }

    @Override
    public List<BankResponseDTO> findAll() {
        List<Bank> banks = bankRepository.findAll();
        List<BankResponseDTO> bankResponseDTOs = banks.stream().map(
                bank -> modelMapper.map(bank, BankResponseDTO.class)).toList();
        return bankResponseDTOs;
    }

    public List<BankResponseDTO> findAllActive() {
        List<Bank> banks = bankRepository.findAllActive();
        List<BankResponseDTO> bankResponseDTOs = banks.stream().map(
                bank -> modelMapper.map(bank, BankResponseDTO.class)).toList();
        return bankResponseDTOs;
    }

    @Override
    public Optional<BankResponseDTO> findById(Long id) {
        Optional<Bank> bank = bankRepository.findById(id);
        if (bank.isPresent()) {
            Optional<BankResponseDTO> bankResponseDTO = bank.map(
                    bankOptional -> modelMapper.map(bankOptional, BankResponseDTO.class));
            return bankResponseDTO;
        }
        return Optional.empty();
    }

    public List<BankResponseDTO> findByName(String name) {
        List<Bank> banks = bankRepository.findByNameContainingIgnoreCaseAndIsDeleteFalse(name);
        List<BankResponseDTO> bankResponseDTOs = banks.stream().map(
                bank -> modelMapper.map(bank, BankResponseDTO.class)).toList();
        return bankResponseDTOs;
    }

    public Page<Bank> findByNameContaining(String name, int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return bankRepository.findByNameContainingIgnoreCaseAndIsDeleteFalse(name, pageable);
    }

    @Override
    public BankResponseDTO save(BankRequestDTO bankRequestDTO) {
        Bank bank = new Bank();
        modelMapper.map(bankRequestDTO, bank);
        bank.setCreatedBy(dumpAuthServiceImpl.getDetails().getId());
        Bank savedBank = bankRepository.save(bank);
        BankResponseDTO bankResponseDTO = modelMapper.map(savedBank, BankResponseDTO.class);
        return bankResponseDTO;
    }

    @Override
    public BankResponseDTO update(BankRequestDTO bankRequestDTO, Long id) {
        Optional<Bank> optionalBank = bankRepository.findById(id);
        if (optionalBank.isPresent()) {
            Bank bank = optionalBank.get();
            modelMapper.map(bankRequestDTO, bank);
            bank.setModifiedBy(dumpAuthServiceImpl.getDetails().getId());
            Bank updatedBank = bankRepository.save(bank);
            return modelMapper.map(updatedBank, BankResponseDTO.class);
        }
        throw new RuntimeException("Bank not found");
    }

    @Override
    public void deleteById(Long id) {
        bankRepository.deleteById(id);
    }

    public void softDeleteById(Long id) {
        Optional<Bank> optionalBank = bankRepository.findById(id);
        if (optionalBank.isPresent()) {
            Bank bank = optionalBank.get();
            bank.setIsDelete(true);
            bank.setDeletedOn(LocalDateTime.now());
            bank.setDeletedBy(dumpAuthServiceImpl.getDetails().getId());
            bankRepository.save(bank);
        } else {
            throw new RuntimeException("Bank not found");
        }
    }

}
