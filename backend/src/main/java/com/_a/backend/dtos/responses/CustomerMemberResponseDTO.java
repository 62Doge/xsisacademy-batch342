package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class CustomerMemberResponseDTO {
    
    private Long id;
    private Long parentBiodataId;
    private Long customerId;
    private Long customerRelationId;

}
