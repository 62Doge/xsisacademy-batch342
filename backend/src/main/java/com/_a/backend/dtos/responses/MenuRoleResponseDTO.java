package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class MenuRoleResponseDTO {
    private Long id;
    private MenuResponseDTO menu;
    private Long menuId;
    private RoleResponseDTO role;
    private Long roleId;
}
