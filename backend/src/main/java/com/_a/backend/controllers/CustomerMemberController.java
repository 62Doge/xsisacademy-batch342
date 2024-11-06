package com._a.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com._a.backend.dtos.responses.CustomerMemberResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.CustomerMemberServiceImpl;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/customer-member")
@CrossOrigin("http://localhost:9002")
public class CustomerMemberController {

    @Autowired
    private CustomerMemberServiceImpl customerMemberService;

    @GetMapping("")
    public ResponseEntity<?> getAllCustomerMembers() {
        try {
            List<CustomerMemberResponseDTO> customerMemberResponseDTOs = customerMemberService.findAll();
            ApiResponse<List<CustomerMemberResponseDTO>> successResponse = new ApiResponse<List<CustomerMemberResponseDTO>>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), customerMemberResponseDTOs);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            ApiResponse<List<CustomerMemberResponseDTO>> errorResponse = new ApiResponse<List<CustomerMemberResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<?> deleteCustomerMemberById(@PathVariable Long id) {
        try {
            Optional<CustomerMemberResponseDTO> responseDTO = customerMemberService.findById(id);
            if (responseDTO.isEmpty()) {
                ApiResponse<CustomerMemberResponseDTO> notFoundResponse = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Customer Member Not Found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
            customerMemberService.softDeleteById(id);
            ApiResponse<CustomerMemberResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "Customer Member deleted successfully", null);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<CustomerMemberResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete Customer Member", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }
    
}
