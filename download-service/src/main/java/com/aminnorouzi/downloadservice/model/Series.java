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
public class Series {

    private Long id;
    private String title;

    private List<Season> seasons;
}
