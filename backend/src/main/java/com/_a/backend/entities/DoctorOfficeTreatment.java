package com._a.backend.entities;

import java.util.List;

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
@Table(name = "t_doctor_office_treatment")
public class DoctorOfficeTreatment extends BaseEntity{
  
  public DoctorOfficeTreatment(Long doctorTreatmentId, Long doctorOfficeId) {
    this.doctorTreatmentId = doctorTreatmentId;
    this.doctorOfficeId = doctorOfficeId;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "doctor_treatment_id", insertable = false, updatable = false)
  @JsonManagedReference
  private DoctorTreatment doctorTreatment;

  @Column(name = "doctor_treatment_id")
  private Long doctorTreatmentId;

  @ManyToOne
  @JoinColumn(name = "doctor_office_id", insertable = false, updatable = false)
  @JsonManagedReference
  private DoctorOffice doctorOffice;

  @Column(name = "doctor_office_id")
  private Long doctorOfficeId;

  @OneToMany(mappedBy = "doctorOfficeTreatment", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<Appointment> appointments;

}
