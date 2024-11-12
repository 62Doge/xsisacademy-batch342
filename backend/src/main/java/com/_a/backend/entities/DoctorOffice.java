package com._a.backend.entities;

import java.time.LocalDate;
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
@Table(name = "t_doctor_office")
public class DoctorOffice extends BaseEntity{
  
  public DoctorOffice(Long doctorId, Long medicalFacilityId, String specialization, LocalDate startDate, LocalDate endDate, Long serviceUnitId) {
    this.doctorId = doctorId;
    this.medicalFacilityId = medicalFacilityId;
    this.specialization = specialization;
    this.startDate = startDate;
    this.endDate = endDate;
    this.serviceUnitId = serviceUnitId;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "specialization", length = 100, nullable = false)
  private String specialization;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @ManyToOne
  @JoinColumn(name = "doctor_id", insertable = false, updatable = false)
  @JsonManagedReference
  private Doctor doctor;

  @Column(name = "doctor_id")
  private Long doctorId;

  @ManyToOne
  @JoinColumn(name="medical_facility_id", insertable = false, updatable = false)
  @JsonManagedReference
  private MedicalFacility medicalFacility;

  @Column(name = "medical_facility_id")
  private Long medicalFacilityId;

  @ManyToOne
  @JoinColumn(name = "service_unit_id", insertable = false, updatable = false)
  @JsonManagedReference
  private ServiceUnit serviceUnit;

  @Column(name = "service_unit_id")
  private Long serviceUnitId;

  @OneToMany(mappedBy = "doctorOffice", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<DoctorOfficeTreatment> doctorOfficeTreatments;

  @OneToMany(mappedBy = "doctorOffice", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<Appointment> appointments;
}
