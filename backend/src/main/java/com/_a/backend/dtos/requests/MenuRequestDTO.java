package com._a.backend.dtos.requests;

import lombok.Data;

@Data
public class MenuRequestDTO {
    private String name;
    private String url;
    private Long parentId;
    private String bigIcon;
    private String smallIcon;
    private Long createdBy;
    private Long ModifiedBy;
    private Long deletedBy;
}
