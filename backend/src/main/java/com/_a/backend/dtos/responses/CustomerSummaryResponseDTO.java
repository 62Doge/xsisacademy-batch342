package com._a.backend.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSummaryResponseDTO {
  private Long userId;
  private List<CustomerMemberSummaryResponseDTO> customerMemberList;
}
