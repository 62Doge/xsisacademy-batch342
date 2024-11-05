package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.MenuRole;

public interface MenuRoleRepository extends JpaRepository<MenuRole, Long> {
    Boolean existsByMenuId(Long menuId);

    Page<MenuRole> findByMenuIdContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, Long menuId);

    Page<MenuRole> findAllByIsDeleteFalse(Pageable pageable);

    List<MenuRole> findAllByIsDeleteFalse();
}
