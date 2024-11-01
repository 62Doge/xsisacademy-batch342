package com._a.backend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "m_doctor_education")
public class DoctorEducation extends BaseEntity{
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "institution_name", length = 100)
  private String institutionName;

  @Column(name = "major", length = 100)
  private String major;

  @Column(name = "start_year", length = 4)
  private Integer startYear;

  @Column(name = "end_year", length = 4)
  private Integer endYear;

  @Column(name = "is_last_education")
  private Boolean isLastEducation;

  @ManyToOne
  @JoinColumn(name = "doctor_id", insertable = false, updatable = false)
  @JsonManagedReference
  private Doctor doctor;

  @Column(name = "doctor_id")
  private Long doctorId;

  @ManyToOne
  @JoinColumn(name = "education_level_id", insertable = false, updatable = false)
  @JsonManagedReference
  private EducationLevel educationLevel;

  @Column(name = "education_level_id")
  private Long educationLevelId;


}
