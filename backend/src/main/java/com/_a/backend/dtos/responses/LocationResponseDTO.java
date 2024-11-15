package com._a.backend.dtos.responses;

import lombok.Data;

@Data
public class LocationResponseDTO {
    private Long id;
    private String name;
    private Long parentId;
    private LocationResponseDTO parent;
    // private List<LocationChildResponseDTO> childs;

    // @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    // @JsonBackReference
    // private List<LocationChildResponseDTO> childs;

    // @JsonProperty("childLocationLevelIds") // This makes it appear in JSON as "childIds"
    // public List<Long> getChildLocationLevelIds() {
    //     return childs.stream().map(LocationChildResponseDTO::getLocationLevelId).collect(Collectors.toList());
    // }

    private Long locationLevelId;
    private LocationLevelResponseDTO locationLevel;
}
