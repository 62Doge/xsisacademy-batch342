package com._a.backend.dtos.responses;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MenuResponseDTO {
    private Long id;
    private String name;
    private String url;
    private String bigIcon;
    private String smallIcon;
    private Long parentId;
    private MenuChildResponseDTO parent;
    
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<MenuChildResponseDTO> childs;

    @JsonProperty("childIds") // This makes it appear in JSON as "childIds"
    public List<Long> getChildIds() {
        return childs.stream().map(MenuChildResponseDTO::getId).collect(Collectors.toList());
    }
}
