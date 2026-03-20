package com.finsight.fin.news.api.dto;

import com.finsight.fin.news.domain.NewsArticle;

import java.time.LocalDateTime;

public record NewsArticleResponse(
        Long id,
        String crawlSource,
        String keyword,
        String title,
        String publisher,
        String sourceHost,
        String googleNewsUrl,
        String originalUrl,
        String normalizedOriginalUrl,
        String content,
        LocalDateTime crawledAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static NewsArticleResponse from(NewsArticle article) {
        return new NewsArticleResponse(
                article.getId(),
                article.getCrawlSource(),
                article.getKeyword(),
                article.getTitle(),
                article.getPublisher(),
                article.getSourceHost(),
                article.getGoogleNewsUrl(),
                article.getOriginalUrl(),
                article.getNormalizedOriginalUrl(),
                article.getContent(),
                article.getCrawledAt(),
                article.getCreatedAt(),
                article.getUpdatedAt()
        );
    }
}
