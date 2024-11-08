package com._a.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.RoleRequestDTO;
import com._a.backend.dtos.requests.SearchRoleRequestDTO;
import com._a.backend.dtos.responses.PaginatedResponseDTO;
import com._a.backend.dtos.responses.RoleResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.RoleRepository;
import com._a.backend.services.impl.PaginationWithSortRequestDTO;
import com._a.backend.services.impl.RoleServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/role")
@CrossOrigin("*")
public class RoleController {
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping({ "", "/" })
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<RoleResponseDTO>>> getRolesWithSearchAndPagination(
            SearchRoleRequestDTO searchRoleRequestDTO,
            PaginationWithSortRequestDTO paginateRequestDTO) {
        Page<RoleResponseDTO> roles = roleService.getAllWithSearch(searchRoleRequestDTO, paginateRequestDTO);
        PaginatedResponseDTO<RoleResponseDTO> responseDTO = new PaginatedResponseDTO<>(roles);
        ApiResponse<PaginatedResponseDTO<RoleResponseDTO>> apiResponse = ApiResponse.success(200, responseDTO);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<?> createRole(@RequestBody RoleRequestDTO roleRequestDTO) {
        RoleResponseDTO responseDTO = roleService.save(roleRequestDTO);
        ApiResponse<RoleResponseDTO> apiResponse = new ApiResponse<>(201, "Created Role successfully!", responseDTO);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
}
