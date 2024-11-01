package com._a.backend.dtos.requests;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserRequestDTO {
    private Long biodataId;
    private Long roleId;
    private String email;
    private String password;
    private int loginAttempt;
    private boolean isLocked;
    private LocalDateTime lastLogin;
}
