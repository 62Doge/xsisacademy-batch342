package com._a.backend.entities;

import java.util.Date;

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
@Table(name = "t_appointment")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Appointment extends BaseEntity{

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "appointment_date")
  private Date appointmentDate;

  @ManyToOne
  @JoinColumn(name = "customer_id", insertable = false, updatable = false)
  @JsonManagedReference
  private Customer customer;

  @Column(name = "customer_id")
  private Long customerId;

  @ManyToOne
  @JoinColumn(name = "doctor_office_id",  insertable = false, updatable = false)
  @JsonManagedReference
  private DoctorOffice doctorOffice;

  @Column(name = "doctor_office_id")
  private Long doctorOfficeId;

  @ManyToOne
  @JoinColumn(name = "doctor_office_schedule_id",  insertable = false, updatable = false)
  @JsonManagedReference
  private DoctorOfficeSchedule doctorOfficeSchedule;

  @Column(name = "doctor_office_schedule_id")
  private Long doctorOfficeScheduleId;

  @ManyToOne
  @JoinColumn(name = "doctor_office_treatment_id",  insertable = false, updatable = false)
  @JsonManagedReference
  private DoctorOfficeTreatment doctorOfficeTreatment;

  @Column(name = "doctor_office_treatment_id")
  private Long doctorOfficeTreatmentId;
  
}
