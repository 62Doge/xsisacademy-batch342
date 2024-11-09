package com._a.backend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.BiodataAddressRequestDTO;
import com._a.backend.dtos.responses.BiodataAddressResponseDTO;
import com._a.backend.entities.BiodataAddress;
import com._a.backend.repositories.BiodataAddressRepository;
import com._a.backend.services.Services;

import jakarta.transaction.Transactional;

@Service
public class BiodataAddressServiceImpl implements Services<BiodataAddressRequestDTO, BiodataAddressResponseDTO> {

    @Autowired
    BiodataAddressRepository biodataAddressRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Page<BiodataAddressResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) { //find all without deleted
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<BiodataAddress> biodataAddresss = biodataAddressRepository.findAllByIsDeleteFalse(pageable);
        Page<BiodataAddressResponseDTO> biodataAddressResponseDTOS = biodataAddresss
                .map(biodataAddress -> modelMapper.map(biodataAddress, BiodataAddressResponseDTO.class));
        return biodataAddressResponseDTOS;
    }

    public Page<BiodataAddressResponseDTO> getAllByBiodataId(int pageNo, int pageSize, String sortBy, String sortDirection, Long biodataId) { //find all without deleted
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<BiodataAddress> biodataAddresss = biodataAddressRepository.findAllByBiodataIdAndIsDeleteFalse(pageable, biodataId);
        Page<BiodataAddressResponseDTO> biodataAddressResponseDTOS = biodataAddresss
                .map(biodataAddress -> modelMapper.map(biodataAddress, BiodataAddressResponseDTO.class));
        return biodataAddressResponseDTOS;
    }

    @Override
    public void deleteById(Long id) {
        biodataAddressRepository.deleteById(id);
    }

    @Override
    public List<BiodataAddressResponseDTO> findAll() {
        // using less optimal approach, but easier to understand
        return biodataAddressRepository.findAll().stream()
                .map(biodataAddress -> modelMapper.map(biodataAddress, BiodataAddressResponseDTO.class))
                .collect(Collectors.toList());
    }

    public Page<BiodataAddressResponseDTO> findAll(int pageNo, int pageSize, String sortBy, String sortDirection) { //find all with deleted
        // using less optimal approach, but easier to understand
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<BiodataAddress> biodataAddresss = biodataAddressRepository.findAll(pageable);
        Page<BiodataAddressResponseDTO> biodataAddressResponseDTOS = biodataAddresss
                .map(biodataAddress -> modelMapper.map(biodataAddress, BiodataAddressResponseDTO.class));
        return biodataAddressResponseDTOS;
    }

    public List<String> findAllLabelsByBiodataIdInAndIsDeleteFalse(List<Long> ids) {
        return biodataAddressRepository.findAllLabelsByBiodataIdInAndIsDeleteFalse(ids);
    }

    @Override
    public Optional<BiodataAddressResponseDTO> findById(Long id) {
        return biodataAddressRepository.findById(id)
                .map(biodataAddress -> modelMapper.map(biodataAddress, BiodataAddressResponseDTO.class));
    }

    public Page<BiodataAddressResponseDTO> searchByBiodataIdAndKeyword(int pageNo, int pageSize, String sortBy, String sortDirection,
            Long biodataId, String keyword) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<BiodataAddress> biodataAddresss = biodataAddressRepository.searchByBiodataIdAndKeyword(pageable, biodataId, keyword);
        Page<BiodataAddressResponseDTO> biodataAddressResponseDTOS = biodataAddresss
                .map(biodataAddress -> modelMapper.map(biodataAddress, BiodataAddressResponseDTO.class));
        return biodataAddressResponseDTOS;
    }

    @Override
    public BiodataAddressResponseDTO save(BiodataAddressRequestDTO biodataAddressRequestDTO) {
        BiodataAddress biodataAddress = biodataAddressRepository.save(modelMapper.map(biodataAddressRequestDTO, BiodataAddress.class));
        return modelMapper.map(biodataAddress, BiodataAddressResponseDTO.class);
    }

    @Transactional
    @Override
    public BiodataAddressResponseDTO update(BiodataAddressRequestDTO biodataAddressRequestDTO, Long id) {
        BiodataAddress biodataAddress = biodataAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Biodata address not found"));

        modelMapper.map(biodataAddressRequestDTO, biodataAddress);
        BiodataAddress updatedBiodataAddress = biodataAddressRepository.save(biodataAddress);

        return modelMapper.map(updatedBiodataAddress, BiodataAddressResponseDTO.class);
    }

    public void softDeleteBiodataAddress(Long id, Long userId) {
        BiodataAddress biodataAddress = biodataAddressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Biodata Address not found"));

        biodataAddress.setIsDelete(true);
        biodataAddress.setDeletedBy(userId);
        biodataAddress.setDeletedOn(LocalDateTime.now());
        biodataAddressRepository.save(biodataAddress);
    }

    public void softDeleteBiodataAddresses(List<Long> ids, Long userId) {
        List<BiodataAddress> biodataAddresses = biodataAddressRepository.findAllById(ids);

        if (biodataAddresses.isEmpty()) throw new IllegalArgumentException("Biodata Address not found");

        biodataAddresses.forEach(biodataAddress -> {
            biodataAddress.setIsDelete(true);
            biodataAddress.setDeletedBy(userId);
            biodataAddress.setDeletedOn(LocalDateTime.now());
        });
        biodataAddressRepository.saveAll(biodataAddresses);
    }
}
