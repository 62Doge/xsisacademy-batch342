package com._a.backend.dtos.responses;

import java.time.LocalDateTime;

import com._a.backend.entities.Biodata;
import com._a.backend.entities.Role;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private Long biodataId;
    private Biodata biodata;
    private Long roleId;
    private Role role;
    private String email;
    private String password;
    private int loginAttempt;
    private boolean isLocked;
    private LocalDateTime lastLogin;
}
