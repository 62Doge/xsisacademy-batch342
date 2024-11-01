package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class BiodataResponseDTO {
    private Long id;
    private String fullname;
    private String mobilePhone;
    private byte[] image;
    private String imagePath;
}
