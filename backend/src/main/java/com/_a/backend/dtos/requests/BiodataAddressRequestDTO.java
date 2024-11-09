package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class BiodataAddressRequestDTO {
    private Long biodataId;
    private String label;
    private String recipient;
    private String recipientPhoneNumber;
    private Long locationId;
    private String postalCode;
    private String address;
    private Long createdBy;
    private Long modifiedBy;
    private Long deletedBy;
}
