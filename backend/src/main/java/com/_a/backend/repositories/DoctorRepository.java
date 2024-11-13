package com._a.backend.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._a.backend.dtos.projections.DoctorDetailsEducationProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsHeaderProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsOfficeHistoryProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsOfficeLocationProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsPriceStartProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsScheduleProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsTreatmentProjectionDto;
import com._a.backend.dtos.projections.DoctorDetailsYOEProjectionDto;
import com._a.backend.entities.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

  @Query(value= "SELECT * FROM m_doctor WHERE is_delete = false AND id = ?1", nativeQuery = true)
  List<Doctor> findByDoctorId(Long id);

  Page<Doctor> findAllByIsDeleteFalse(Pageable pageable);

  Doctor findTopByOrderByStrDesc();

  @Query(value = "SELECT b.fullname as name, s.name AS specialization " +
                  "FROM m_doctor d " +
                  "JOIN m_biodata b ON d.biodata_id = b.id " +
                  "JOIN t_current_doctor_specialization cds ON cds.doctor_id = d.id " +
                  "JOIN m_specialization s ON cds.specialization_id = s.id " +
                  "WHERE d.id = :doctor_id", nativeQuery = true)
  DoctorDetailsHeaderProjectionDto getHeader(@Param("doctor_id") Long doctorId);

  @Query(value = "SELECT DISTINCT d.id, " + 
                  "EXTRACT(YEAR FROM CURRENT_DATE) - " + 
                  "EXTRACT(YEAR FROM (SELECT MIN(t_doctor_office.start_date) FROM m_doctor JOIN t_doctor_office ON t_doctor_office.doctor_id = m_doctor.id WHERE m_doctor.id = :doctor_id)) AS year_of_experience " +
                  "FROM m_doctor d " + 
                  "JOIN t_doctor_office tdo ON tdo.doctor_id = d.id " + 
                  "WHERE d.id = :doctor_id", nativeQuery = true)
  DoctorDetailsYOEProjectionDto getYearOfExperience(@Param("doctor_id") Long doctorId);

  @Query(value = "SELECT name " + 
                  "FROM t_doctor_treatment " + 
                  "WHERE doctor_id = :doctor_id", nativeQuery = true)
  List<DoctorDetailsTreatmentProjectionDto> getTreatment(@Param("doctor_id") Long doctorId);

  @Query(value = "SELECT institution_name as name, major, end_year as year " + //
                  "FROM m_doctor_education " + //
                  "WHERE doctor_id = :doctor_id", nativeQuery = true)
  List<DoctorDetailsEducationProjectionDto> getEducation(@Param("doctor_id") Long doctorId);

  @Query(value = "SELECT mmf.name as name, city.name as location, tdo.specialization, tdo.start_date, tdo.end_date " +
                  "FROM m_doctor md " +
                  "JOIN t_doctor_office tdo on tdo.doctor_id = md.id " +
                  "JOIN m_medical_facility mmf on mmf.id = tdo.medical_facility_id " +
                  "JOIN m_location subdistrict ON subdistrict.id = mmf.location_id " +
                  "LEFT JOIN m_location city ON subdistrict.parent_id = city.id " +
                  "WHERE md.id = :doctor_id", nativeQuery = true)
  List<DoctorDetailsOfficeHistoryProjectionDto> getOfficeHistory(@Param("doctor_id") Long doctorId);

  @Query(value = "SELECT mmfs.day, mmfs.time_schedule_start as start_time, mmfs.time_schedule_end as end_time " + 
                  "FROM m_medical_facility_schedule mmfs " + 
                  "JOIN t_doctor_office_schedule tdos ON tdos.medical_facility_schedule_id = mmfs.id " + 
                  "WHERE tdos.doctor_id = :doctor_id and mmfs.medical_facility_id = :medical_facility_id", nativeQuery = true)
  List<DoctorDetailsScheduleProjectionDto> getSchedule(@Param("medical_facility_id") Long medicalFacilityId, @Param("doctor_id") Long doctorId);

  @Query(value = "SELECT COALESCE(MIN(price_start_from), 0.0) as price_start " + 
                  "FROM t_doctor_office tdo " + 
                  "JOIN t_doctor_office_treatment tdot ON tdot.doctor_office_id = tdo.id " + 
                  "JOIN t_doctor_treatment tdt ON tdot.doctor_treatment_id = tdt.id " + 
                  "JOIN t_doctor_office_treatment_price tdotp ON tdotp.doctor_office_treatment_id = tdot.id " + 
                  "WHERE tdo.doctor_id = :doctor_id AND tdo.medical_facility_id = :medical_facility_id " + 
                  "GROUP BY tdo.doctor_id, tdo.medical_facility_id", nativeQuery = true)
  DoctorDetailsPriceStartProjectionDto getPriceStart(@Param("medical_facility_id") Long medicalFacilityId, @Param("doctor_id") Long doctorId);

  @Query(value =  "SELECT " +
                  "mmf.id AS medical_facility_id, " + 
                  "mmf.name AS medical_facility_name, " +
                  "msu.name AS service_unit_name, " +
                  "mmf.full_address AS address, " +
                  "subdistrict.name AS subdistrict, " +
                  "city.name AS city " +
                  "FROM m_doctor md " +
                  "JOIN t_doctor_office tdo ON tdo.doctor_id = md.id " +
                  "JOIN m_medical_facility mmf ON mmf.id = tdo.medical_facility_id " +
                  "JOIN m_service_unit msu ON msu.id = tdo.service_unit_id " +
                  "JOIN m_location subdistrict ON subdistrict.id = mmf.location_id " +
                  "LEFT JOIN m_location city ON subdistrict.parent_id = city.id " +
                  "WHERE md.id = :doctor_id AND tdo.end_date IS NULL", nativeQuery = true)
  List<DoctorDetailsOfficeLocationProjectionDto> getOfficeLocation(@Param("doctor_id") Long doctorId);

}
