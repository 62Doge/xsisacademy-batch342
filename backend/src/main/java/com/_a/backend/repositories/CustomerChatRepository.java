package com._a.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com._a.backend.dtos.projections.PatientChatNumberProjectionDTO;
import com._a.backend.entities.CustomerChat;

@Repository
public interface CustomerChatRepository extends JpaRepository<CustomerChat, Long> {
    @Query(value = "SELECT COUNT(*) as chat_number " +
                    "FROM t_customer_chat tcc " +
                    "WHERE tcc.customer_id = :customer_id", nativeQuery = true)
    PatientChatNumberProjectionDTO getChatNumber(@Param("customer_id") Long customerId);
}
