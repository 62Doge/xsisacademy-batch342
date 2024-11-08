package com._a.backend.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com._a.backend.dtos.requests.RoleRequestDTO;
import com._a.backend.dtos.requests.SearchTextRequestDTO;
import com._a.backend.dtos.responses.RoleResponseDTO;
import com._a.backend.entities.Role;
import com._a.backend.exceptions.IdNotFoundException;
import com._a.backend.repositories.RoleRepository;
import com._a.backend.services.AuthService;
import com._a.backend.services.Services;

import jakarta.transaction.Transactional;

@Service
public class RoleServiceImpl implements Services<RoleRequestDTO, RoleResponseDTO> {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthService authService;

    public Page<RoleResponseDTO> getAllWithSearch(SearchTextRequestDTO requestDTO,
            PaginationWithSortRequestDTO paginate) {
        int pageNo = paginate.getPageNo();
        int pageSize = paginate.getPageSize();
        String sortBy = paginate.getSortBy();
        String sortDirection = paginate.getSortDirection();

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return roleRepository.findAllWithSearch(
                requestDTO.getSearchText(), pageable)
                .map(role -> new RoleResponseDTO(role));
    }

    @Override
    public Page<RoleResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public List<RoleResponseDTO> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    @Override
    public Optional<RoleResponseDTO> findById(Long id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    @Override
    public RoleResponseDTO save(RoleRequestDTO roleRequestDTO) {
        Long userId = authService.getDetails().getId();

        Role role = new Role();
        role.setName(roleRequestDTO.getName());
        role.setCode(roleRequestDTO.getCode());
        role.setCreatedBy(userId);

        role = roleRepository.save(role);
        return new RoleResponseDTO(role);
    }

    @Transactional
    @Override
    public RoleResponseDTO update(RoleRequestDTO roleRequestDTO, Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Role with id: " + id + " not found"));
        Long userId = authService.getDetails().getId();

        role.setName(roleRequestDTO.getName());
        role.setCode(roleRequestDTO.getCode());
        role.setModifiedBy(userId);

        role = roleRepository.save(role);
        return new RoleResponseDTO(role);
    }

    public void softDelete(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IdNotFoundException("Role with id: " + id + " not found"));

        Long userId = authService.getDetails().getId();

        role.setIsDelete(true);
        role.setDeletedBy(userId);
        role.setDeletedOn(LocalDateTime.now());
        roleRepository.save(role);
    }

}
