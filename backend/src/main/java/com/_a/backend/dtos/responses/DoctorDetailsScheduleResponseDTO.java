package com._a.backend.dtos.responses;

import java.util.List;

import lombok.Data;

@Data
public class DoctorDetailsScheduleResponseDTO {
    
    public DoctorDetailsScheduleResponseDTO(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    private List<Schedule> schedules;

    @Data
    public static class Schedule {
        public Schedule(String day, String startTime, String endTime) {
            this.day = day;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        private String day;
        private String startTime;
        private String endTime;
    }
    
}
