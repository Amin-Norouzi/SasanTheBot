package com.aminnorouzi.downloadservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
public class Provider {

    private String title;
    private String address;
    private Boolean isFiltered;
}
