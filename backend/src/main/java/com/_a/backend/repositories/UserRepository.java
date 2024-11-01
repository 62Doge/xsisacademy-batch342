package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com._a.backend.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
