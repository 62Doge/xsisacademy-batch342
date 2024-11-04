package com._a.backend.dtos.responses;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
public class PaginatedResponseDTO<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private String sortBy;
    private String sortDirection;

    public PaginatedResponseDTO(Page<T> pageData) {
        this.content = pageData.getContent();
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.totalElements = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();
        extractSortByAndSortDirection(pageData);
    }

    public void extractSortByAndSortDirection(Page<T> pageData) {
        Sort.Order order = pageData.getSort().stream().findFirst().orElse(null);
        if (order != null) {
            this.sortBy = order.getProperty();
            this.sortDirection = order.getDirection().toString();
        } else {
            this.sortBy = "id";
            this.sortDirection = "asc";
        }
    }
}
