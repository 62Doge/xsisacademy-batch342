package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class MenuRoleRequestDTO {
    private Long menuId;
    private Long roleId;
    private Long createdBy;
    private Long modifiedBy;
    private Long deletedBy;
}
