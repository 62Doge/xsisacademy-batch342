package com._a.backend.dtos.requests;

import java.util.List;

import lombok.Data;

@Data
public class BiodataAddressIdsRequestDTO {
    List<Long> ids;
    Long deletedBy;
}
