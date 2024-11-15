package com._a.backend.services.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.multipart.MultipartFile;

import com._a.backend.dtos.requests.BiodataRequestDTO;
import com._a.backend.dtos.responses.BiodataResponseDTO;
import com._a.backend.entities.Biodata;
import com._a.backend.repositories.BiodataRepository;
import com._a.backend.services.Services;

import jakarta.transaction.Transactional;

@Service
public class BiodataServiceImpl implements Services<BiodataRequestDTO, BiodataResponseDTO> {

    @Autowired
    BiodataRepository biodataRepository;
    @Autowired
    ModelMapper modelMapper;

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

}
