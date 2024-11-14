package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class PatientChatNumberResponseDTO {
    
    public PatientChatNumberResponseDTO(Integer chatNumber) {
        this.chatNumber = chatNumber;
    }

    private Integer chatNumber;

}
