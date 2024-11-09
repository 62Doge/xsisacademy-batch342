package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private Long biodataId;
    private BiodataResponseDTO biodata;
    private Long roleId;
    private RoleResponseDTO role;
    private String email;
    // private String password;
    // private int loginAttempt;
    // private boolean isLocked;
    // private LocalDateTime lastLogin;
}
