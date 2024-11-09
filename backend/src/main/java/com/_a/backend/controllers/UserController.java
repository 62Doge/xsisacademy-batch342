package com._a.backend.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.responses.UserResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.UserServiceImpl;

@RestController
@RequestMapping("/api/admin/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(@PathVariable("id") Long id) {
        Optional<UserResponseDTO> user = userService.findById(id);

        if (user.isPresent()) {
            return ResponseEntity
                    .ok(ApiResponse.success(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), user.get()));
        } else {
            ApiResponse<UserResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                    "User not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
        }
    }

}
