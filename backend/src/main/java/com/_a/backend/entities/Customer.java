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

  public Customer() {

  }

  public Customer(Long biodataId, LocalDate dob, String gender, Long bloodGroupId, String rhesus_type, Double height, Double weight) {
    this.biodataId = biodataId;
    this.dob = dob;
    this.gender = gender;
    this.bloodGroupId = bloodGroupId;
    this.rhesus_type = rhesus_type;
    this.height = height;
    this.weight = weight;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "biodata_id", insertable = false, updatable = false)
  private Biodata biodata;

  @Column(name = "biodata_id")
  private Long biodataId;

  @Column(name = "dob")
  private LocalDate dob;

  @Column(name = "gender", length = 1)
  private String gender;

  @ManyToOne
  @JoinColumn(name = "blood_group_id", insertable = false, updatable = false)
  private BloodGroup bloodGroup;

  @Column(name = "blood_group_id")
  private Long bloodGroupId;

  @Column(name = "rhesus_type", length = 5)
  private String rhesus_type;

  @Column(name = "height")
  private Double height;

  @Column(name = "weight")
  private Double weight;
  
}
