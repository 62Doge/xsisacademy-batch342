package com._a.backend.services;

import java.util.List;

import com._a.backend.dtos.projections.CustomerCustomNominalProjectionDto;
import com._a.backend.entities.CustomerCustomNominal;

public interface CustomerCustomNominalService {
  CustomerCustomNominal create(int nominal);

  List<CustomerCustomNominalProjectionDto> getNominalLessThanEqualBalance();
}
