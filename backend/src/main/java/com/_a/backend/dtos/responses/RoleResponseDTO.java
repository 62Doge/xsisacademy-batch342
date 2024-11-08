package com._a.backend.dtos.responses;

import com._a.backend.entities.Role;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleResponseDTO {
    private Long id;
    private String name;
    private String code;

    public RoleResponseDTO(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.code = role.getCode();
    }
}
