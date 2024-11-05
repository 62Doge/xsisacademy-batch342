package com._a.backend.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "m_role")
@Data
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 20)
  private String name;

  @Column(length = 20)
  private String code;

  @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<User> users;

  @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<MenuRole> menuRoles;
}
