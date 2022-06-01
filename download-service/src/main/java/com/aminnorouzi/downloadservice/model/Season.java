package com.aminnorouzi.downloadservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Builder
@Getter
@Setter
public class Season {

    private Integer number;
    private Integer episodes;

    private List<String> qualities;
    private List<Link> links;
}
