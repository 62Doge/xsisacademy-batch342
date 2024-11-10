package com._a.backend.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._a.backend.dtos.projections.PatientProjectionDto;
import com._a.backend.entities.CustomerMember;

@Repository
public interface CustomerMemberRepository extends JpaRepository<CustomerMember, Long> {

        Boolean existsByCustomerRelationIdAndParentBiodataIdAndIsDeleteFalse(Long customerRelationId, Long parentBiodataId);

        Optional<CustomerMember> findByCustomerRelationIdAndParentBiodataIdAndIsDeleteFalse(Long customerRelationId, Long parentBiodataId);

        Page<CustomerMember> findAllByIsDeleteFalse(Pageable page);

        List<CustomerMember> findAllByParentBiodataIdAndIsDeleteFalse(Long parentBiodataId);

        @Query(value = "SELECT cm.id, cm.parent_biodata_id, b.fullname, c.dob, c.gender, c.blood_group_id, c.rhesus_type as rhesus, cm.customer_relation_id "
                        +
                        "FROM m_customer_member cm " +
                        "JOIN m_customer c on c.id = cm.customer_id " +
                        "JOIN m_biodata b on b.id = c.biodata_id " +
                        "WHERE " +
                        "cm.is_delete = false AND " +
                        "cm.parent_biodata_id = :parent_biodata_id", nativeQuery = true)
        Page<PatientProjectionDto> findPatient(@Param("parent_biodata_id") Long parentBiodataId, Pageable pageable);

        @Query(value = "SELECT cm.id, cm.parent_biodata_id, b.fullname, c.dob, c.gender, c.height, c.weight, c.blood_group_id, c.rhesus_type as rhesus, cm.customer_relation_id "
                        +
                        "FROM m_customer_member cm " +
                        "JOIN m_customer c on c.id = cm.customer_id " +
                        "JOIN m_biodata b on b.id = c.biodata_id " +
                        "WHERE " +
                        "cm.is_delete = false AND " +
                        "cm.parent_biodata_id = :parent_biodata_id AND " +
                        "b.fullname ILIKE CONCAT('%', :name, '%')", nativeQuery = true)
        Page<PatientProjectionDto> findPatientByName(@Param("name") String name, @Param("parent_biodata_id") Long parentBiodataId, Pageable pageable);
        
}
