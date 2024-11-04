package com._a.backend.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface Services<RequestDTO, ResponseDTO> {
//    for pagination
//    Page<ResponseDTO> getAll(int pageNo, int pageSize, String sortBy, String sortDirection);
    List<ResponseDTO> findAll();
    Optional<ResponseDTO> findById(Long id);
    ResponseDTO save(RequestDTO requestDTO);
    ResponseDTO update(RequestDTO requestDTO, Long id);
    void deleteById(Long id);
}
