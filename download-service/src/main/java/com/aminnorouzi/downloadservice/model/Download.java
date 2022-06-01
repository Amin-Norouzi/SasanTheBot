package com.aminnorouzi.downloadservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Download {

    private String title;
    private String url;
    private Type type;
    private Provider provider;
    private Movie movie;
    private Series series;
}
