package com.finsight.fin.news.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record GoogleFinanceCrawlRequest(
        @Min(1) @Max(20) Integer maxScrollCount,
        @Min(1) @Max(200) Integer maxArticlesPerRun
) {
}
