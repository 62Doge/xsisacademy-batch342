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
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "t_doctor_treatment")
public class DoctorTreatment extends BaseEntity {
  public DoctorTreatment(String name){
    this.name = name;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", length = 50)
  private String name;

  @ManyToOne
  @JoinColumn(name ="doctor_id", insertable = false, updatable=false)
  @JsonManagedReference
  private Doctor doctor;

  @Column(name = "doctor_id")
  private Long doctorId;

  @OneToMany(mappedBy = "doctorTreatment", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<DoctorOfficeTreatment> doctorOfficeTreatments ;
}
