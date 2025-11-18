package com.newscrawling.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrawlResult {
    private String url;
    private String title;
    private String content;
    private int httpStatus;
    private boolean success;
}