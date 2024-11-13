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

import com._a.backend.dtos.requests.MedicalItemCategoryRequestDTO;
import com._a.backend.dtos.responses.MedicalItemCategoryResponseDTO;
import com._a.backend.entities.MedicalItemCategory;
import com._a.backend.repositories.MedicalItemCategoryRepository;
import com._a.backend.services.Services;

@Service
public class MedicalItemCategoryServiceImpl implements Services<MedicalItemCategoryRequestDTO, MedicalItemCategoryResponseDTO>{
    @Autowired
    MedicalItemCategoryRepository medicalItemCategoryRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public void deleteById(Long id) {
        medicalItemCategoryRepository.deleteById(id);        
    }

    @Override
    public List<MedicalItemCategoryResponseDTO> findAll() {
        return medicalItemCategoryRepository.findAll().stream()
                .map(medicalItemCategory -> modelMapper.map(medicalItemCategory, MedicalItemCategoryResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MedicalItemCategoryResponseDTO> findById(Long id) {
        return medicalItemCategoryRepository.findById(id)
                .map(medicalItemCategory -> modelMapper.map(medicalItemCategory, MedicalItemCategoryResponseDTO.class));
    }

    @Override
    public Page<MedicalItemCategoryResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(sortBy);
        sort = sortDirection.equalsIgnoreCase("desc") ? sort.descending() : sort.ascending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<MedicalItemCategory> medicalItemCategorys = medicalItemCategoryRepository.findAllByIsDeleteFalse(pageable);
        Page<MedicalItemCategoryResponseDTO> medicalItemCategoryResponseDTOS = medicalItemCategorys
                .map(medicalItemCategory -> modelMapper.map(medicalItemCategory, MedicalItemCategoryResponseDTO.class));
        return medicalItemCategoryResponseDTOS;
    }

    @Override
    public MedicalItemCategoryResponseDTO save(MedicalItemCategoryRequestDTO medicalItemCategoryRequestDTO) {
        MedicalItemCategory medicalItemCategory = medicalItemCategoryRepository.save(modelMapper.map(medicalItemCategoryRequestDTO, MedicalItemCategory.class));
        return modelMapper.map(medicalItemCategory, MedicalItemCategoryResponseDTO.class);
    }

    @Override
    public MedicalItemCategoryResponseDTO update(MedicalItemCategoryRequestDTO medicalItemCategoryRequestDTO, Long id) {
        MedicalItemCategory medicalItemCategory = medicalItemCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical Item not found"));

        modelMapper.map(medicalItemCategoryRequestDTO, medicalItemCategory);
        MedicalItemCategory updatedMedicalItemCategory = medicalItemCategoryRepository.save(medicalItemCategory);

        return modelMapper.map(updatedMedicalItemCategory, MedicalItemCategoryResponseDTO.class);
    }

    public void softDeleteMedicalItemCategory(Long id, Long userId) {
        MedicalItemCategory medicalItemCategory = medicalItemCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MedicalItemCategory not found"));

        boolean hasActiveMedicalItems = medicalItemCategory.getMedicalItems().stream()
                .anyMatch(medicalItem -> !medicalItem.getIsDelete());

        if (hasActiveMedicalItems) {
            throw new IllegalStateException("Cannot delete location level: it's active on locations.");
        }

        medicalItemCategory.setIsDelete(true);
       medicalItemCategory.setDeletedBy(userId);
        medicalItemCategory.setDeletedOn(LocalDateTime.now());
        medicalItemCategoryRepository.save(medicalItemCategory);
    }
    
}
