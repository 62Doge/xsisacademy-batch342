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
@Table(name = "m_medical_facility_schedule")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedicalFacilitySchedule extends BaseEntity {
  public MedicalFacilitySchedule(Long medicalFacilityId, String day, String timeScheduleStart, String timeScheduleEnd) {
    this.medicalFacilityId = medicalFacilityId;
    this.day = day;
    this.timeScheduleStart = timeScheduleStart;
    this.timeScheduleEnd = timeScheduleEnd;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "day")
  private String day;

  @Column(name = "time_schedule_start")
  private String timeScheduleStart;
  
  @Column(name = "time_schedule_end")
  private String timeScheduleEnd;

  @ManyToOne
  @JoinColumn(name = "medical_facility_id", insertable = false, updatable = false)
  @JsonManagedReference
  private MedicalFacility medicalFacility;
  
  @Column(name = "medical_facility_id")
  private Long medicalFacilityId;

  @OneToMany(mappedBy = "medicalFacilitySchedule", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<DoctorOfficeSchedule> doctorOfficeSchedules;

  
}
