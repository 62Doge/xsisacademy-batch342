package com._a.backend.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PaymentMethodRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;
}
