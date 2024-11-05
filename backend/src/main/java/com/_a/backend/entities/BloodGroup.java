package com._a.backend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "m_blood_group")
@Data
@EqualsAndHashCode(callSuper = true)
public class BloodGroup extends BaseEntity {

  public BloodGroup() {

  }

  public BloodGroup(String code, String description) {
    this.code = code;
    this.description = description;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @Column(length = 5)
  private String code;

  @Column(length = 255)
  private String description;

}
