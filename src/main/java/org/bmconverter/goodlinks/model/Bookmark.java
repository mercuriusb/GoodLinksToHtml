package org.bmconverter.goodlinks.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Bookmark(
    String title,
    String summary,
    String url,
    List<String> tags
){
}
