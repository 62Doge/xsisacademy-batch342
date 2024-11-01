package com._a.backend.dtos.responses;

import com._a.backend.entities.Menu;
import com._a.backend.entities.Role;

import lombok.Data;

@Data
public class MenuRoleResponseDTO {
    private Long id;
    private Menu menu;
    private Long menuId;
    private Role role;
    private Long roleId;
}
