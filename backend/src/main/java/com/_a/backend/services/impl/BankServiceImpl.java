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
    private BankRepository bankRepository;

    @Autowired
    private ModelMapper modelMapper;

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

    @Override
    public BankResponseDTO save(BankRequestDTO bankRequestDTO) {
        Bank bank = bankRepository.save(modelMapper.map(bankRequestDTO, Bank.class));
        BankResponseDTO bankResponseDTO = modelMapper.map(bank, BankResponseDTO.class);
        return bankResponseDTO;
    }

    @Override
    public BankResponseDTO update(BankRequestDTO bankRequestDTO, Long id) {
        Optional<Bank> optionalBank = bankRepository.findById(id);
        if (optionalBank.isPresent()) {
            Bank bank = optionalBank.get();
            modelMapper.map(bankRequestDTO, bank);
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
            bankRepository.save(bank);
        } else {
            throw new RuntimeException("Bank not found");
        }
    }

    public Page<Bank> findActiveBankPages(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return bankRepository.findAllByIsDeleteFalse(pageable);
    }

}
