package com._a.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "m_user")
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "biodata_id", insertable = false, updatable = false)
  private Biodata biodata;

  @Column(name = "biodata_id")
  private Long biodataId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "role_id", insertable = false, updatable = false)
  private Role role;

  @Column(name = "role_id")
  private Long roleId;

  @Column(name = "email", length = 100)
  private String email;

  @Column(name = "password", length = 255)
  private String password;

  @Column(name = "login_attempt")
  private int loginAttempt;

  @Column(name = "is_locked")
  private boolean isLocked;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_login")
  private LocalDateTime lastLogin;
}
