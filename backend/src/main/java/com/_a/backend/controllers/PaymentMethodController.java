package com._a.backend.controllers;

import com._a.backend.dtos.requests.PaymentMethodRequestDTO;
import com._a.backend.dtos.responses.PaginatedResponseDTO;
import com._a.backend.dtos.responses.PaymentMethodResponseDTO;
import com._a.backend.payloads.ApiResponse;
import com._a.backend.services.impl.PaymentMethodServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/admin/payment-method")
@CrossOrigin("*")
public class PaymentMethodController {
    @Autowired
    private PaymentMethodServiceImpl paymentMethodService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<PaymentMethodResponseDTO>>> getPaymentMethods(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        try {
            Page<PaymentMethodResponseDTO> paymentMethodResponseDTOS =
                    paymentMethodService.getAll(pageNo, pageSize, sortBy, sortDirection);
            PaginatedResponseDTO<PaymentMethodResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(paymentMethodResponseDTOS);

            ApiResponse<PaginatedResponseDTO<PaymentMethodResponseDTO>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }catch (Exception e){
            ApiResponse<PaginatedResponseDTO<PaymentMethodResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPaymentMethodById(@PathVariable("id") Long id) {
        try {
            Optional<PaymentMethodResponseDTO> paymentMethod = paymentMethodService.findById(id);

            if (paymentMethod.isPresent()) {
                ApiResponse<PaymentMethodResponseDTO> successResponse =
                        new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paymentMethod.get());
                return ResponseEntity.status(HttpStatus.OK).body(successResponse);
            } else {
                ApiResponse<PaymentMethodResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Payment Method not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }
        }catch (Exception e) {
            ApiResponse<PaymentMethodResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ApiResponse<PaginatedResponseDTO<PaymentMethodResponseDTO>>> getPaymentMethodByName(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "3") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @PathVariable String name) {
        try {
            Page<PaymentMethodResponseDTO> paymentMethodResponseDTOS =
                    paymentMethodService.getByName(pageNo, pageSize, sortBy, sortDirection, name);

            PaginatedResponseDTO<PaymentMethodResponseDTO> paginatedResponse = new PaginatedResponseDTO<>(paymentMethodResponseDTOS);

            ApiResponse<PaginatedResponseDTO<PaymentMethodResponseDTO>> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paginatedResponse);
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
        }catch (Exception e) {
            ApiResponse<PaginatedResponseDTO<PaymentMethodResponseDTO>> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> savePaymentMethod(@Valid @RequestBody PaymentMethodRequestDTO paymentMethodRequestDTO) {
        try {
            PaymentMethodResponseDTO paymentMethodResponseDTOSaved = paymentMethodService.save(paymentMethodRequestDTO);

            ApiResponse<PaymentMethodResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paymentMethodResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<PaymentMethodResponseDTO> alreadyExistResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(alreadyExistResponse);
        } catch (Exception e) {
            ApiResponse<PaymentMethodResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to save Payment Method", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePaymentMethod(@Valid @RequestBody PaymentMethodRequestDTO paymentMethodRequestDTO, @PathVariable Long id) {
        try {
            PaymentMethodResponseDTO paymentMethodResponseDTOSaved = paymentMethodService.update(paymentMethodRequestDTO, id);

            ApiResponse<PaymentMethodResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), paymentMethodResponseDTOSaved);
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            ApiResponse<PaymentMethodResponseDTO> badRequestResponse =
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (Exception e) {
            ApiResponse<PaymentMethodResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to update Payment Method", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @PatchMapping("/soft-delete/{id}")
    public ResponseEntity<?> softDeletePaymentMethod(@PathVariable Long id) {
        try {
            paymentMethodService.softDeletePaymentMethod(id);

            ApiResponse<PaymentMethodResponseDTO> successResponse =
                    new ApiResponse<>(HttpStatus.OK.value(), "Payment Method soft deleted", null);
            return ResponseEntity.ok(successResponse);
        }catch (IllegalArgumentException e) {
            ApiResponse<PaymentMethodResponseDTO> badRequestResponse =
                    new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestResponse);
        } catch (IllegalStateException e){
            ApiResponse<PaymentMethodResponseDTO> conflictResponse =
                    new ApiResponse<>(HttpStatus.CONFLICT.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(conflictResponse);
        } catch (Exception e) {
            ApiResponse<PaymentMethodResponseDTO> errorResponse =
                    new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to soft delete Location Level", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeletePaymentMethod(@PathVariable Long id) {
        try{
            Optional<PaymentMethodResponseDTO> paymentMethodResponseDTO = paymentMethodService.findById(id);

            if (paymentMethodResponseDTO.isEmpty()) {
                ApiResponse<PaymentMethodResponseDTO> notFoundResponse =
                        new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Payment Method not found", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundResponse);
            }

            paymentMethodService.deleteById(id);
            ApiResponse<PaymentMethodResponseDTO> successResponse = new ApiResponse<>(HttpStatus.OK.value(), "Payment Method deleted", null);
            return ResponseEntity.ok(successResponse);
        } catch (Exception e) {
            ApiResponse<PaymentMethodResponseDTO> errorResponse = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to delete Payment Method", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(errorResponse);
        }
    }


}
