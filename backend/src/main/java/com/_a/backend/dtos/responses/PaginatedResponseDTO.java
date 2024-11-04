package com._a.backend.dtos.responses;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PaginatedResponseDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PaginatedResponseDTO(Page<T> pageData) {
        this.content = pageData.getContent();
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.totalElements = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();
    }
}
