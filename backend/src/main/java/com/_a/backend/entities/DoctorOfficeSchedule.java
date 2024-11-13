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
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "t_doctor_office_schedule")
public class DoctorOfficeSchedule extends BaseEntity {
  public DoctorOfficeSchedule(Integer slot, Long doctorId, Long medicalFacilityScheduleId) {
    this.slot = slot;
    this.doctorId = doctorId;
    this.medicalFacilityScheduleId = medicalFacilityScheduleId;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "slot")
  private Integer slot;

  @ManyToOne
  @JoinColumn(name = "doctor_id", insertable = false, updatable = false)
  @JsonManagedReference
  private Doctor doctor;

  @Column(name = "doctor_id")
  private Long doctorId;

  @ManyToOne
  @JoinColumn(name = "medical_facility_schedule_id",  insertable = false, updatable = false)
  @JsonManagedReference
  private MedicalFacilitySchedule medicalFacilitySchedule;

  @Column(name = "medical_facility_schedule_id")
  private Long medicalFacilityScheduleId;

  @OneToMany(mappedBy = "doctorOfficeSchedule", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<Appointment> appointments;
}
