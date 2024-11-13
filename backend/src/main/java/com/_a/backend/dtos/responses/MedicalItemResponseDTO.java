package com._a.backend.dtos.responses;

import com._a.backend.entities.MedicalItemCategory;
import com._a.backend.entities.MedicalItemSegmentation;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MedicalItemResponseDTO {
    private String id;
    private String name;
    private Long medicalItemCategoryId;
    private Long medicalItemSegmentationId;
    @JsonBackReference
    private MedicalItemCategory medicalItemCategory;
    @JsonBackReference
    private MedicalItemSegmentation medicalItemSegmentation;
    @JsonProperty("categoryName") // This makes it appear in JSON as "childIds"
    public String getMedicalItemCategoryName() {
        return medicalItemCategory.getName();
    }
    @JsonProperty("segmentationName") // This makes it appear in JSON as "childIds"
    public String getMedicalItemSegmentationName() {
        return medicalItemSegmentation.getName();
    }
    // private String composition;
    // private String manufacturer;
    private String indication;
    // private String dosage;
    // private String directions;
    // private String contraindication;
    // private String caution;
    private String packaging;
    private Long priceMax;
    private Long priceMin;
    private String imagePath;
}
