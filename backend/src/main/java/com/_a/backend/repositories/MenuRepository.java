package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}
