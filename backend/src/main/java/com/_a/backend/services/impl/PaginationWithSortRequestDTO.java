package com._a.backend.services.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationWithSortRequestDTO {
  private int pageNo;
  private int pageSize = 10;
  private String sortBy = "id";
  private String sortDirection = "asc";
}
