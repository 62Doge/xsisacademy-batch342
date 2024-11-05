package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Boolean existsByName(String name);

    Page<Menu> findByNameContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String name);

    Page<Menu> findAllByIsDeleteFalse(Pageable pageable);

    List<Menu> findAllByIsDeleteFalse();
}
