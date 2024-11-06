package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class CustomerMemberRequestDTO {
    
    private Long id;
    private Long parentBiodataId;
    private Long customerId;
    private Long customerRelationId;

}
