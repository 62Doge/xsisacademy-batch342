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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name= "m_doctor")
public class Doctor extends BaseEntity {
  public Doctor(String str){
    this.str = str;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "str", length = 50)
  private String str;

  @OneToOne
  @JoinColumn(name = "biodata_id", insertable = false, updatable = false)
  @JsonManagedReference
  private Biodata biodata;

  @Column(name = "biodata_id")
  private Long biodataId;

  @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<DoctorTreatment> doctorTreatments;

  @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<DoctorOffice> doctorOffices;

  @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<CurrentDoctorSpecialization> currentDoctorSpecializations;
  
  @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<DoctorEducation> doctorEducations;
}
