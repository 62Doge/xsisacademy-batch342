package com._a.backend.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_prescription")
@AllArgsConstructor
@NoArgsConstructor
public class Prescription extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "appointment_id")
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_id", insertable = false, updatable = false)
    private Appointment appointment;

    @Column(name = "medical_item_id")
    private Long medicalItemId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medical_item_id", insertable = false, updatable = false)
    private MedicalItem medicalItem;

    @Column(name = "dosage", columnDefinition = "TEXT")
    private String dosage;

    @Column(name = "directions", columnDefinition = "TEXT")
    private String directions;

    @Column(name = "time", length = 100)
    private String time;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "printed_on", updatable = false)
    private LocalDateTime printedOn;

    @Column(name = "print_attempt")
    private Integer printAttempt;
}
