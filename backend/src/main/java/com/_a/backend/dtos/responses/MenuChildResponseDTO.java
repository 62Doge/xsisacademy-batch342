package com._a.backend.dtos.responses;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuChildResponseDTO {
    private Long id;
    private String name;
    private Long parentId;
}
