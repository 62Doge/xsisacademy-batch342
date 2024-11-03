package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class BankResponseDTO {
    
    Long id;
    String name;
    String vaCode;
    Boolean isDelete;

}
