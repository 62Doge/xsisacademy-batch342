package com._a.backend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.RoleRequestDTO;
import com._a.backend.dtos.responses.PaginatedResponseDTO;
import com._a.backend.dtos.responses.RoleResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.RoleRepository;
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


    @GetMapping("")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<RoleResponseDTO>>> getRoles(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<RoleResponseDTO> roleResponseDTOS =
                        roleService.getAll(pageNo, pageSize, sortBy, sortDirection);
            PaginatedResponseDTO<RoleResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(roleResponseDTOS);

            ApiResponse<PaginatedResponseDTO<RoleResponseDTO>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }catch (Exception e){
            ApiResponse<PaginatedResponseDTO<RoleResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
//    using paging above
//    @GetMapping("")
//    public ResponseEntity<?> findAllRoles() {
//        try {
//            List<RoleResponseDTO> roleResponseDTOS = roleService.findAll();
//
//            ApiResponse<List<RoleResponseDTO>> successResponse =
//                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), roleResponseDTOS);
//            return new ResponseEntity<>(successResponse, HttpStatus.OK);
//        }catch (Exception e) {
//            ApiResponse<List<RoleResponseDTO>> errorResponse =
//                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
//            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable("id") Long id) {
        try {
            Optional<RoleResponseDTO> role = roleService.findById(id);

            if (role.isPresent()) {
                ApiResponse<RoleResponseDTO> successResponse =
                        new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), role.get());
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                ApiResponse<RoleResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Role not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        }catch (Exception e) {
            ApiResponse<RoleResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<RoleResponseDTO>>> getRoleByName(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @PathVariable String name) {
        try {
            Page<RoleResponseDTO> roleResponseDTOS =
                    roleService.getByName(pageNo, pageSize, sortBy, sortDirection, name);

            PaginatedResponseDTO<RoleResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(roleResponseDTOS);

            ApiResponse<PaginatedResponseDTO<RoleResponseDTO>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<RoleResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveRole(@Valid @RequestBody RoleRequestDTO roleRequestDTO) {
        if (roleRepository.existsByName(roleRequestDTO.getName())) {
            ApiResponse<RoleResponseDTO> alreadyExistResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), "Role name already exists", null);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(alreadyExistResponse);
        }

        try {
            RoleResponseDTO roleResponseDTOSaved = roleService.save(roleRequestDTO);
            ApiResponse<RoleResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), roleResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<RoleResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save Role", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRole(@Valid @RequestBody RoleRequestDTO roleRequestDTO, @PathVariable Long id) {
        try {
            RoleResponseDTO roleResponseDTOSaved = roleService.update(roleRequestDTO, id);
            ApiResponse<RoleResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), roleResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<RoleResponseDTO> badRequestResponse =
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (Exception e) {
            ApiResponse<RoleResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update Role", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeleteRole(@PathVariable Long id) {
        try {
            roleService.softDeleteRole(id);

            ApiResponse<RoleResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), "Role soft deleted", null);
            return ResponseEntity.ok(successResponse);
        }catch (IllegalArgumentException e) {
            ApiResponse<RoleResponseDTO> badRequestResponse =
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (IllegalStateException e){
            ApiResponse<RoleResponseDTO> conflictResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflictResponse);
        } catch (Exception e) {
            ApiResponse<RoleResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to soft delete Role", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteRole(@PathVariable Long id) {
        try{
            Optional<RoleResponseDTO> roleResponseDTO = roleService.findById(id);

            if (roleResponseDTO.isEmpty()) {
                ApiResponse<RoleResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Role not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            roleService.deleteById(id);
            ApiResponse<RoleResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "Role deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<RoleResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete Role", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

}
