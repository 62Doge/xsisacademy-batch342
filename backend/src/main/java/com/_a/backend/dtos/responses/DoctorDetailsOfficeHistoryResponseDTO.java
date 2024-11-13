package com._a.backend.dtos.responses;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class DoctorDetailsOfficeHistoryResponseDTO {
    
    public DoctorDetailsOfficeHistoryResponseDTO(List<OfficeHistory> officeHistory) {
        this.officeHistory = officeHistory;
    }

    private List<OfficeHistory> officeHistory;

    @Data
    public static class OfficeHistory {
        public OfficeHistory(String name, String location, String specialization, LocalDate startDate, LocalDate endDate) {
            this.name = name;
            this.location = location;
            this.specialization = specialization;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        private String name;
        private String location;
        private String specialization;
        private LocalDate startDate;
        private LocalDate endDate;

    }

}
