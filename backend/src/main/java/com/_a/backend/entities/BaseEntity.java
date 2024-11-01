package com._a.backend.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {
    
    @Column(name = "created_by", updatable = false)
    private Long createdBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    @Column(name = "modified_by", updatable = true, nullable = true)
    private Long modifiedBy;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modefied_on", updatable = true, nullable = true)
    private LocalDateTime modifiedOn;

    @Column(name = "deleted_by", nullable = true)
    private Long deletedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_on", nullable = true)
    private LocalDateTime deletedOn;

    @Column(name = "is_delete")
    private Boolean isDeleted = false;

}
