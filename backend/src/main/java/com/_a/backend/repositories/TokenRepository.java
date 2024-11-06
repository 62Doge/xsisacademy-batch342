package com._a.backend.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com._a.backend.entities.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
  @Query(value = """
      select t
      from Token t
      where t.email=?1
      and t.token=?2
      and t.isExpired=false
      and t.expiredOn > ?3
      """)
  Optional<Token> findActiveTokenByEmailAndToken(String email, String otp, LocalDateTime now);

  @Query(value = """
      select t
      from Token t
      where t.email=?1
      and t.isExpired=false
      and t.expiredOn > ?2
      ORDER BY t.createdOn DESC
      LIMIT 1
      """)
  Optional<Token> findActiveTokenByEmail(String email, LocalDateTime now);

  @Query("""
        SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END FROM Token t WHERE t.email = ?1 AND t.token = ?2 AND (
        t.expiredOn < ?3
        or t.isExpired=true
        )
      """)
  boolean existsExpiredTokenByEmailAndToken(String email, String token, LocalDateTime now);
  boolean existsByEmail(String email);
}
