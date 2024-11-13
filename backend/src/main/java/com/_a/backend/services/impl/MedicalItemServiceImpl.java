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

import com._a.backend.dtos.requests.MedicalItemRequestDTO;
import com._a.backend.dtos.responses.MedicalItemResponseDTO;
import com._a.backend.entities.MedicalItem;
import com._a.backend.repositories.MedicalItemRepository;
import com._a.backend.services.Services;

@Service
public class MedicalItemServiceImpl implements Services<MedicalItemRequestDTO, MedicalItemResponseDTO> {

    @Autowired
    MedicalItemRepository medicalItemRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public void deleteById(Long id) {
        medicalItemRepository.deleteById(id);
    }

    @Override
    public List<MedicalItemResponseDTO> findAll() {
        return medicalItemRepository.findAll().stream()
                .map(medicalItem -> modelMapper.map(medicalItem, MedicalItemResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MedicalItemResponseDTO> findById(Long id) {
        return medicalItemRepository.findById(id)
                .map(medicalItem -> modelMapper.map(medicalItem, MedicalItemResponseDTO.class));
    }

    @Override
    public Page<MedicalItemResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<MedicalItem> medicalItems = medicalItemRepository.findAllByIsDeleteFalse(pageable);
        Page<MedicalItemResponseDTO> medicalItemResponseDTOS = medicalItems
                .map(medicalItem -> modelMapper.map(medicalItem, MedicalItemResponseDTO.class));
        return medicalItemResponseDTOS;
    }

    @Override
    public MedicalItemResponseDTO save(MedicalItemRequestDTO medicalItemRequestDTO) {
        MedicalItem medicalItem = medicalItemRepository.save(modelMapper.map(medicalItemRequestDTO, MedicalItem.class));
        return modelMapper.map(medicalItem, MedicalItemResponseDTO.class);
    }

    @Override
    public MedicalItemResponseDTO update(MedicalItemRequestDTO medicalItemRequestDTO, Long id) {
        MedicalItem medicalItem = medicalItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical Item not found"));

        modelMapper.map(medicalItemRequestDTO, medicalItem);
        MedicalItem updatedMedicalItem = medicalItemRepository.save(medicalItem);

        return modelMapper.map(updatedMedicalItem, MedicalItemResponseDTO.class);
    }

    public void softDeleteMedicalItem(Long id, Long userId) {
        MedicalItem medicalItem = medicalItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MedicalItem not found"));

        boolean hasActivePrescriptions = medicalItem.getPrescriptions().stream()
                .anyMatch(prescription -> !prescription.getIsDelete());

        if (hasActivePrescriptions) {
            throw new IllegalStateException("Cannot delete location level: it's active on locations.");
        }

        medicalItem.setIsDelete(true);
        medicalItem.setDeletedBy(userId);
        medicalItem.setDeletedOn(LocalDateTime.now());
        medicalItemRepository.save(medicalItem);
    }

    public Page<MedicalItemResponseDTO> searchMedicalItems(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDirection,
            Long medicalItemCategoryId,
            String keyword,
            String segmentationName,
            Double minPrice,
            Double maxPrice) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<MedicalItem> medicalItems = medicalItemRepository.searchMedicalItems(pageable, medicalItemCategoryId, keyword, segmentationName, minPrice, maxPrice);
        Page<MedicalItemResponseDTO> medicalItemResponseDTOS = medicalItems
                .map(medicalItem -> modelMapper.map(medicalItem, MedicalItemResponseDTO.class));
        return medicalItemResponseDTOS;
    }

    public Page<MedicalItemResponseDTO> searchMedicalItemsVersionTwo(
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDirection,
            String categoryName,
            String keyword,
            String segmentationName,
            Double minPrice,
            Double maxPrice) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<MedicalItem> medicalItems = medicalItemRepository.searchMedicalItemsVersionTwo(pageable, categoryName, keyword, segmentationName, minPrice, maxPrice);
        Page<MedicalItemResponseDTO> medicalItemResponseDTOS = medicalItems
                .map(medicalItem -> modelMapper.map(medicalItem, MedicalItemResponseDTO.class));
        return medicalItemResponseDTOS;
    }
}
