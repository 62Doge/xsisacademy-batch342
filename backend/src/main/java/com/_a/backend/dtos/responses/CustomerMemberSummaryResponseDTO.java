package com._a.backend.dtos.responses;

import java.time.LocalDate;

import com._a.backend.entities.CustomerMember;

import lombok.Data;

@Data
public class CustomerMemberSummaryResponseDTO {
  private Long customerId;
  private LocalDate dob;
  private String gender;

  private String fullname;
  private String imagePath;

  private String customerRelationName;

  public CustomerMemberSummaryResponseDTO(CustomerMember customerMember) {
    this.customerId = customerMember.getCustomer().getId();
    this.dob = customerMember.getCustomer().getDob();
    this.gender = customerMember.getCustomer().getGender();

    this.fullname = customerMember.getCustomer().getBiodata().getFullname();
    this.imagePath = customerMember.getCustomer().getBiodata().getImagePath();

    this.customerRelationName = customerMember.getCustomerRelation().getName();
  }
}
