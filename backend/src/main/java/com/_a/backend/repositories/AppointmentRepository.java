package com._a.backend.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._a.backend.dtos.projections.AppointmentExceededDateProjectionDTO;
import com._a.backend.entities.Appointment;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

        @Query("""
                        select count(a) as appointmentCount, a.appointmentDate as appointmentDate, dos.id as doctorOfficeScheduleId
                        from Appointment a
                        join a.doctorOfficeSchedule dos
                        join dos.medicalFacilitySchedule mfs
                        where a.appointmentDate between current_date and :toDateLater
                        and dos.doctorId = :doctorId
                        group by a.appointmentDate, dos.id
                        having count(a) = dos.slot
                        """)
        List<AppointmentExceededDateProjectionDTO> findExceededDates(@Param("doctorId") Long doctorId,
                        @Param("toDateLater") LocalDate toDateLater);

        @Query("""
                        select coalesce((case when count(a) = dos.slot then true else false end), false)
                        from Appointment a
                        join a.doctorOfficeSchedule dos
                        where a.appointmentDate = :appointmentDate
                        and dos.id = :doctorOfficeScheduleId
                        group by dos.slot
                        """)
        Optional<Boolean> isAppointmentCountExceeded(@Param("appointmentDate") LocalDate appointmentDate,
                        @Param("doctorOfficeScheduleId") Long doctorOfficeScheduleId);

        @Query(value = "SELECT * FROM t_appointment WHERE is_delete = false AND doctor_office_id = ?1 AND appointment_date > NOW()", nativeQuery = true)
        List<Appointment> findByOfficeId(Long id);

        @Query("""
                                  select a
                                  from Appointment a
                                  join a.customer c
                                  join c.biodata b
                                  join User u on u.biodataId = b.id
                                  where a.isDelete=false
                        and u.id = ?1
                                          """)
        List<Appointment> findAllByUserId(Long userId);
}
