package com.finsight.fin.news.api.dto;

import jakarta.validation.constraints.NotBlank;

public record NewsArticleUpdateRequest(
        @NotBlank String title,
        String publisher,
        @NotBlank String originalUrl,
        @NotBlank String content
) {
}
