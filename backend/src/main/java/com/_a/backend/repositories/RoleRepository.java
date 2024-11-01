package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
