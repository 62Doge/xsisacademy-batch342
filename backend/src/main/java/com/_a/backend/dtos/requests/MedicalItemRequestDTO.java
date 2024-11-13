package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class MedicalItemRequestDTO {
    private String name;
    private Long medicalItemCategoryId;
    private Long medicalItemSegmentationId;
    private String composition;
    private String manufacturer;
    private String indication;
    private String dosage;
    private String directions;
    private String contraindication;
    private String caution;
    private String packaging;
    private Long priceMax;
    private Long priceMin;
    private byte[] image;
    private String imagePath;
    private Long createdBy;
    private Long modifiedBy;
    private Long deletedBy;
}
