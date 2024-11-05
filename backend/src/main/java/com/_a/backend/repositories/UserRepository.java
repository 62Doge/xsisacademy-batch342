package com._a.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

  Page<User> findByEmailContainingIgnoreCaseAndIsDeleteFalse(Pageable pageable, String email);

  Page<User> findAllByIsDeleteFalse(Pageable pageable);

  List<User> findAllByIsDeleteFalse();
}
