package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.ResetPassword;

public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {

}
