package com._a.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.responses.DoctorTreatmentResponseDTO;
import com._a.backend.dtos.responses.MenuRoleResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.MenuRoleServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/transaction/menu-role")
@CrossOrigin("*")
public class MenuRoleController {
    @Autowired
    private MenuRoleServiceImpl menuRoleService; 

    @GetMapping("/menuRoleAccess/{roleId}")
    public ResponseEntity<?> menuRoleAccessByRoleId(@PathVariable Long roleId) {
        try {
            List<MenuRoleResponseDTO> menuRoleResponseDTOs = menuRoleService.getMenuAccessByRoleId(roleId);
            if (menuRoleResponseDTOs.isEmpty()) {
                ApiResponse<MenuRoleResponseDTO> notFoundResponse =
                    new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "doctor treatment not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            } else {
                ApiResponse<List<MenuRoleResponseDTO>> successResponse =
                    new ApiResponse<List<MenuRoleResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), menuRoleResponseDTOs);
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            }
        } catch (Exception e) {
        ApiResponse<List<MenuRoleResponseDTO>> errorResponse =
            new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    

    @PostMapping("/save-menu-access/{roleId}")
    public ResponseEntity<?> saveMenuAccess(@PathVariable("roleId") Long roleId, @RequestBody List<Long> selectedMenuIds) {
        // Process the selectedMenuIds (e.g., update MenuRole records)
        menuRoleService.updateMenuAccessForRole(roleId, selectedMenuIds);

        return ResponseEntity.ok("Access updated successfully");
    }

}
