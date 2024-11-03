package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class BankResponseDTO {
    
    private Long id;
    private String name;
    private String vaCode;
    private Boolean isDelete;

}
