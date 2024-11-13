package com._a.backend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.BloodGroupRequestDTO;
import com._a.backend.dtos.responses.BloodGroupResponseDTO;
import com._a.backend.entities.BloodGroup;
import com._a.backend.repositories.BloodGroupRepository;
import com._a.backend.services.Services;

@Service
public class BloodGroupServiceImpl implements Services<BloodGroupRequestDTO, BloodGroupResponseDTO> {

    @Autowired
    BloodGroupRepository bloodGroupRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    DumpAuthServiceImpl dumpAuthServiceImpl;

    @Override
    public Page<BloodGroupResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public List<BloodGroupResponseDTO> findAll() {
        List<BloodGroup> bloodGroups = bloodGroupRepository.findAll();
        List<BloodGroupResponseDTO> bloodGroupResponseDTOs = bloodGroups.stream().map(
            bloodGroup -> modelMapper.map(bloodGroup, BloodGroupResponseDTO.class)).toList();
        return bloodGroupResponseDTOs;
    }

    public List<BloodGroupResponseDTO> findAllActive() {
        List<BloodGroup> bloodGroups = bloodGroupRepository.findAllActive();
        List<BloodGroupResponseDTO> bloodGroupResponseDTOs = bloodGroups.stream().map(
            bloodGroup -> modelMapper.map(bloodGroup, BloodGroupResponseDTO.class)).toList();
        return bloodGroupResponseDTOs;
    }

    @Override
    public Optional<BloodGroupResponseDTO> findById(Long id) {
        Optional<BloodGroup> bloodGroup = bloodGroupRepository.findById(id);
        if (bloodGroup.isPresent()) {
            Optional<BloodGroupResponseDTO> bloodGroupResponseDTO = bloodGroup.map(
                bloodGroupOptional -> modelMapper.map(bloodGroupOptional, BloodGroupResponseDTO.class));
                return bloodGroupResponseDTO;
        }
        return Optional.empty();
    }

    @Override
    public BloodGroupResponseDTO save(BloodGroupRequestDTO bloodGroupRequestDTO) {
        BloodGroup bloodGroup = new BloodGroup();
        modelMapper.map(bloodGroupRequestDTO, bloodGroup);
        bloodGroup.setCreatedBy(dumpAuthServiceImpl.getDetails().getId());
        BloodGroup savedBloodGroup = bloodGroupRepository.save(bloodGroup);
        BloodGroupResponseDTO bloodGroupResponseDTO = modelMapper.map(savedBloodGroup, BloodGroupResponseDTO.class);
        return bloodGroupResponseDTO;
    }

    @Override
    public BloodGroupResponseDTO update(BloodGroupRequestDTO bloodGroupRequestDTO, Long id) {
        Optional<BloodGroup> optionalBloodGroup = bloodGroupRepository.findById(id);
        if (optionalBloodGroup.isPresent()) {
            BloodGroup bloodGroup = optionalBloodGroup.get();
            modelMapper.map(bloodGroupRequestDTO, bloodGroup);
            bloodGroup.setModifiedBy(dumpAuthServiceImpl.getDetails().getId());
            BloodGroup updatedBloodGroup = bloodGroupRepository.save(bloodGroup);
            return modelMapper.map(updatedBloodGroup, BloodGroupResponseDTO.class);
        }
        throw new RuntimeException("Blood Group not found");
    }

    @Override
    public void deleteById(Long id) {
        bloodGroupRepository.deleteById(id);
    }

    public void softDeleteById(Long id) {
        Optional<BloodGroup> optionalBloodGroup = bloodGroupRepository.findById(id);
        if (optionalBloodGroup.isPresent()) {
            BloodGroup bloodGroup = optionalBloodGroup.get();
            bloodGroup.setIsDelete(true);
            bloodGroup.setDeletedOn(LocalDateTime.now());
            bloodGroup.setDeletedBy(dumpAuthServiceImpl.getDetails().getId());
            bloodGroupRepository.save(bloodGroup);
        } else {
            throw new RuntimeException("Blood group not found");
        }
    }
    
}
