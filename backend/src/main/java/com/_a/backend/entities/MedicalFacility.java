package com._a.backend.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "m_medical_facility")
public class MedicalFacility extends BaseEntity {

  public MedicalFacility(String name, String fullAdress, String email, String phoneCode, String phone, String fax) {
    this.name = name;
    this.fullAddress = fullAdress;
    this.email = email;
    this.phoneCode = phoneCode;
    this.phone = phone;
    this.fax = fax;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", length = 50)
  private String name;

  @Column(name = "full_address")
  private String fullAddress;

  @Column(name = "email", length = 100)
  private String email;

  @Column(name = "phone_code", length = 10)
  private String phoneCode;

  @Column(name = "phone", length = 15)
  private String phone;

  @Column(name = "fax", length = 15)
  private String fax;

  @ManyToOne
  @JoinColumn(name = "medical_facility_category_id", insertable = false, updatable = false)
  @JsonManagedReference
  private MedicalFacilityCategory medicalFacilityCategory;

  @Column(name = "medical_facility_category_id")
  private Long medicalFacilityCategoryId;

  @ManyToOne
  @JoinColumn(name = "location_id", insertable = false, updatable = false)
  @JsonManagedReference
  private Location location;

  @Column(name = "location_id")
  private Long locationId;

  @OneToMany(mappedBy = "medicalFacility", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<DoctorOffice> doctorOffices;

  @OneToMany(mappedBy = "medicalFacility", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<MedicalFacilitySchedule> medicalFacilitySchedules;
}
