package com._a.backend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "m_customer_member")
@EqualsAndHashCode(callSuper = true)
@Data
public class CustomerMember extends BaseEntity {
    
    public CustomerMember() {

    }

    public CustomerMember(Long parentBiodataId, Long customerId, Long customerRelationId) {
        this.parentBiodataId = parentBiodataId;
        this.customerId = customerId;
        this.customerRelationId = customerRelationId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_biodata_id")
    private Long parentBiodataId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;
    
    @Column(name = "customer_id")
    private Long customerId;

    @ManyToOne
    @JoinColumn(name = "customer_relation_id", insertable = false, updatable = false)
    private CustomerRelation customerRelation;

    @Column(name = "customer_relation_id")
    private Long customerRelationId;

}
