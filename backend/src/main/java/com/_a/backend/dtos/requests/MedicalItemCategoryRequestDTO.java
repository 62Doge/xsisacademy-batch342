package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class MedicalItemCategoryRequestDTO {
    private String name;
    private Long createdBy;
    private Long modifiedBy;
    private Long deletedBy;
}
