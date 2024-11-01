package com._a.backend.dtos.responses;

import com._a.backend.entities.Menu;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuResponseDTO {
    private Long id;
    private String name;
    private String url;
    private Long parentId;
    private Menu parent;
    private String bigIcon;
    private String smallIcon;
}
