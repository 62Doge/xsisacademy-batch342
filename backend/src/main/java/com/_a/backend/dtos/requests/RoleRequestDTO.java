package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class RoleRequestDTO {
    private String name;
    private String code;
    private Long createdBy;
}
