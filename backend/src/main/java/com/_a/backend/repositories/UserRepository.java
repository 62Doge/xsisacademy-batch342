package com._a.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._a.backend.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("""
      SELECT u 
      FROM User u 
      WHERE u.email = :email
      """)
    Optional<User> findByEmail(@Param("email") String email);

    @Query("""
      SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END 
      FROM User u 
      WHERE u.email = :email
      """)
    Boolean existsByEmail(@Param("email") String email);

    @Query("""
      SELECT u 
      FROM User u 
      WHERE LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')) 
      AND u.isDelete = FALSE
      """)
    Page<User> findByEmailContainingIgnoreCaseAndIsDeleteFalse(@Param("email") String email, Pageable pageable);

    @Query("""
      SELECT u 
      FROM User u 
      WHERE u.isDelete = FALSE
      """)
    Page<User> findAllByIsDeleteFalse(Pageable pageable);

    @Query("""
      SELECT u 
      FROM User u 
      WHERE u.isDelete = FALSE
      """)
    List<User> findAllByIsDeleteFalse();
}
