package com._a.backend.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    // Static helper methods for common responses
    public static <T> ApiResponse<T> success(int status, T data) {
        return new ApiResponse<>(status, "success", data);
    }

    public static <T> ApiResponse<Exception> error(int status, Exception e) {
        return new ApiResponse<>(status, "error", e);
    }
}
