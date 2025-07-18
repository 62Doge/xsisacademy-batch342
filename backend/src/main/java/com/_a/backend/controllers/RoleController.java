package com._a.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.RoleRequestDTO;
import com._a.backend.dtos.requests.SearchTextRequestDTO;
import com._a.backend.dtos.responses.PaginatedResponseDTO;
import com._a.backend.dtos.responses.RoleResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.PaginationWithSortRequestDTO;
import com._a.backend.services.impl.RoleServiceImpl;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/admin/role")
@CrossOrigin("*")
public class RoleController {
    @Autowired
    private RoleServiceImpl roleService;

    @GetMapping({ "", "/" })
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<RoleResponseDTO>>> getRolesWithSearchAndPagination(
            SearchTextRequestDTO searchTextRequestDTO,
            PaginationWithSortRequestDTO paginateRequestDTO) {
        Page<RoleResponseDTO> roles = roleService.getAllWithSearch(searchTextRequestDTO, paginateRequestDTO);
        PaginatedResponseDTO<RoleResponseDTO> responseDTO = new PaginatedResponseDTO<>(roles);
        ApiResponse<PaginatedResponseDTO<RoleResponseDTO>> apiResponse = ApiResponse.success(200, responseDTO);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping({ "{id}", "{id}/" })
    public ResponseEntity<ApiResponse<RoleResponseDTO>> getById(@PathVariable Long id) {
        RoleResponseDTO responseDTO = roleService.findById(id).orElse(null);

        return new ResponseEntity<>(ApiResponse.success(200, responseDTO), HttpStatus.OK);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<?> createRole(@RequestBody @Valid RoleRequestDTO roleRequestDTO) {
        RoleResponseDTO responseDTO = roleService.save(roleRequestDTO);
        ApiResponse<RoleResponseDTO> apiResponse = new ApiResponse<>(201, "Created Role successfully!", responseDTO);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping({ "{id}", "{id}/" })
    public ResponseEntity<ApiResponse<RoleResponseDTO>> updateRole(@PathVariable Long id,
            @RequestBody @Valid RoleRequestDTO roleRequestDTO) {
        RoleResponseDTO role = roleService.update(roleRequestDTO, id);
        ApiResponse<RoleResponseDTO> apiResponse = new ApiResponse<RoleResponseDTO>(200, "Update Role successfully!",
                role);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping({ "soft-delete/{id}", "soft-delete/{id}/" })
    public ResponseEntity<ApiResponse<Void>> softDeleteRole(@PathVariable Long id) {
        roleService.softDelete(id);
        ApiResponse<Void> apiResponse = new ApiResponse<Void>(200, "Delete Role successfully!", null);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
