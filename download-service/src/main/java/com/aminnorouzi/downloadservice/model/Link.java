package com.aminnorouzi.downloadservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
public class Link {

    private String quality;
    private String size;
    private String url;
}
