package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class BiodataRequestDTO {
    private String fullname;
    private String mobilePhone;
    private byte[] image;
    private String imagePath;
}
