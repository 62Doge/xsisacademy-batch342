package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Boolean existsByName(String name);

    Page<Role> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);

    Page<Role> findAllByIsDeleteFalse(Pageable pageable);

    List<Role> findAllByIsDeleteFalse();
}
