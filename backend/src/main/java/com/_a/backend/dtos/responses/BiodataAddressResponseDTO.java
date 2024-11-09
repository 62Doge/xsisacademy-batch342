package com._a.backend.dtos.responses;
import lombok.Data;

@Data
public class BiodataAddressResponseDTO {
    private Long id;
    private Long biodataId;
    private BiodataResponseDTO biodata;
    private String label;
    private String recipient;
    private String recipientPhoneNumber;
    private Long locationId;
    private LocationResponseDTO location;
    private String postalCode;
    private String address;
}
