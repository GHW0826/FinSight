package com.finsight.fin.news.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@Table(name = "news_articles")
@Comment("Stores crawled and manually managed news articles.")
public class NewsArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("Primary key.")
    private Long id;

    @Column(nullable = false, length = 100)
    @Comment("Logical crawl source. Example: GOOGLE_NEWS_FINANCE.")
    private String crawlSource;

    @Column(nullable = false, length = 100)
    @Comment("Search keyword used for this article.")
    private String keyword;

    @Column(nullable = false, length = 500)
    @Comment("Article title.")
    private String title;

    @Column(length = 255)
    @Comment("Publisher label shown by Google News or derived from host.")
    private String publisher;

    @Column(length = 255)
    @Comment("Host of the original article URL.")
    private String sourceHost;

    @Column(nullable = false, length = 1000)
    @Comment("Google News article URL.")
    private String googleNewsUrl;

    @Column(nullable = false, length = 1000)
    @Comment("Original publisher URL.")
    private String originalUrl;

    @Column(nullable = false, unique = true, length = 1000)
    @Comment("Normalized original publisher URL used for duplicate prevention.")
    private String normalizedOriginalUrl;

    @Lob
    @Column(nullable = false)
    @Comment("Full crawled article body.")
    private String content;

    @Column(nullable = false)
    @Comment("When the article body was crawled.")
    private LocalDateTime crawledAt;

    @Column(nullable = false)
    @Comment("Entity creation timestamp.")
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Comment("Entity last update timestamp.")
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public String getCrawlSource() {
        return crawlSource;
    }

    public void setCrawlSource(String crawlSource) {
        this.crawlSource = crawlSource;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getSourceHost() {
        return sourceHost;
    }

    public void setSourceHost(String sourceHost) {
        this.sourceHost = sourceHost;
    }

    public String getGoogleNewsUrl() {
        return googleNewsUrl;
    }

    public void setGoogleNewsUrl(String googleNewsUrl) {
        this.googleNewsUrl = googleNewsUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getNormalizedOriginalUrl() {
        return normalizedOriginalUrl;
    }

    public void setNormalizedOriginalUrl(String normalizedOriginalUrl) {
        this.normalizedOriginalUrl = normalizedOriginalUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCrawledAt() {
        return crawledAt;
    }

    public void setCrawledAt(LocalDateTime crawledAt) {
        this.crawledAt = crawledAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.crawledAt == null) {
            this.crawledAt = now;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
