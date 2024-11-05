package com._a.backend.entities;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "m_customer")
@EqualsAndHashCode(callSuper = true)
@Data
public class Customer extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "biodata_id", insertable = false, updatable = false)
  private Biodata biodata;

  @Column(name = "biodata_id")
  private Long biodataId;

  private LocalDate dob;

  @Column(length = 1)
  private String gender;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "blood_group_id", insertable = false, updatable = false)
  private BloodGroup bloodGroup;

  @Column(name = "blood_group_id")
  private Long bloodGroupId;

  @Column(length = 5)
  private String rhesus_type;

  private Double height;

  private Double weight;
}
