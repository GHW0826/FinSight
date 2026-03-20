package com.finsight.fin.news.api.dto;

import jakarta.validation.constraints.NotBlank;

public record NewsArticleCreateRequest(
        @NotBlank String crawlSource,
        @NotBlank String keyword,
        @NotBlank String title,
        String publisher,
        @NotBlank String googleNewsUrl,
        @NotBlank String originalUrl,
        @NotBlank String content
) {
}
