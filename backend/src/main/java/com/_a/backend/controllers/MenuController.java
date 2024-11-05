package com._a.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.requests.MenuRequestDTO;
import com._a.backend.dtos.responses.MenuResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.repositories.MenuRepository;
import com._a.backend.services.impl.MenuServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/menu")
@CrossOrigin("*")
public class MenuController {
    @Autowired
    private MenuServiceImpl menuService;
    @Autowired
    private MenuRepository menuRepository;

    @GetMapping("")
    public ResponseEntity<?> findAllMenus() {
        List<MenuResponseDTO> menuResponseDTOS = menuService.findAll();
        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), menuResponseDTOS));

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findMenuById(@PathVariable("id") Long id) {
        Optional<MenuResponseDTO> menu = menuService.findById(id);

        if (menu.isPresent()) {
            return ResponseEntity
                    .ok(ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), menu.get()));
        } else {
            ApiResponse<MenuResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                    "Menu not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> saveMenu(@Valid @RequestBody MenuRequestDTO menuRequestDTO) {
        if (menuRepository.existsByName(menuRequestDTO.getName())) {
            ApiResponse<MenuResponseDTO> alreadyExistResponse = new ApiResponse<>(HttpStatus.CONFLICT.value(),
                    "Menu name already exists", null);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(alreadyExistResponse);
        }

        MenuResponseDTO menuResponseDTOSaved = menuService.save(menuRequestDTO);
        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), menuResponseDTOSaved));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMenu(@Valid @RequestBody MenuRequestDTO menuRequestDTO, @PathVariable Long id) {

        if (menuService.findById(id).isEmpty()) {
            ApiResponse<MenuResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                    "Menu not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }

        MenuResponseDTO menuResponseDTOSaved = menuService.update(menuRequestDTO, id);
        return ResponseEntity
                .ok(ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), menuResponseDTOSaved));
    }

    // @PatchMapping("/soft-delete/{id}")

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteMenu(@PathVariable Long id) {

        if (menuService.findById(id).isEmpty()) {
            ApiResponse<MenuResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                    "Menu not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }

        menuService.deleteById(id);
        ApiResponse<MenuResponseDTO> successResponse = new ApiResponse<>(HttpStatus.NO_CONTENT.value(),
                "Menu deleted", null);
        return ResponseEntity.ok(successResponse);
    }

}
