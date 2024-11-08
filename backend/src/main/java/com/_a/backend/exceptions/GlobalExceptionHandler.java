package com._a.backend.exceptions;

import com._a.backend.payloads.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
// When annotated with @ControllerAdvice,
// a class can define methods that handle exceptions thrown by any controller
// method.
// This means you don't have to repeat the error handling logic in every
// controller.
public class GlobalExceptionHandler {

    // When a controller method throws an exception, Spring checks if any method
    // in the current controller has an @ExceptionHandler annotation for that
    // exception type.
    // If no local handlers are found, Spring will look for @ControllerAdvice
    // classes.
    // If a matching @ExceptionHandler is found in any @ControllerAdvice, that
    // method will be invoked.
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Exception>> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(ApiResponse.error(ex.getStatusCode().value(), ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Exception>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        ApiResponse<Map<String, String>> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                "Validation error", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFoundException(UserNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "User not found",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiResponse<String>> handleTokenNotFoundException(InvalidTokenException ex) {
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Token invalid",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleInvalidPasswordException(
            InvalidPasswordException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("password", ex.getMessage());

        ApiResponse<Map<String, String>> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                "Password invalid", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NewPasswordConfirmationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleNewPasswordConfirmationException(
            NewPasswordConfirmationException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("password", ex.getMessage());
        errors.put("confirmPassword", ex.getMessage());

        ApiResponse<Map<String, String>> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(),
                "Password invalid", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(TokenRequestTooSoonException.class)
    public ResponseEntity<ApiResponse<String>> handleTokenRequestTooSoonException(TokenRequestTooSoonException ex) {
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.TOO_MANY_REQUESTS.value(), "Request too soon",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleIdNotFoundException(IdNotFoundException ex) {
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Id not found",
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}