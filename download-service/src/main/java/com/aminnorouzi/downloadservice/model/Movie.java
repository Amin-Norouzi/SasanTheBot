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
public class Movie {

    private Long id;
    private String title;

    private List<Link> links;
}
