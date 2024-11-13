package com._a.backend.dtos.responses;

import java.time.LocalDate;

import com._a.backend.entities.Appointment;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppointmentCustomerDocterOfficeScheduleResponseDTO {
    private Long customerId;
    private Long doctorOfficeScheduleId;
    private LocalDate appointmentDate;

    public AppointmentCustomerDocterOfficeScheduleResponseDTO(Appointment appointment) {
        this.customerId = appointment.getCustomerId();
        this.doctorOfficeScheduleId = appointment.getDoctorOfficeScheduleId();
        this.appointmentDate = appointment.getAppointmentDate();
    }
}
