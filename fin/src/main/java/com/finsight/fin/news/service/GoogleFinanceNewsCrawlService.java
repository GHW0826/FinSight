package com.finsight.fin.news.service;

import com.finsight.crawl.api.CrawlOperations;
import com.finsight.crawl.api.CrawlRequest;
import com.finsight.crawl.api.CrawlResult;
import com.finsight.fin.news.api.dto.GoogleFinanceCrawlRequest;
import com.finsight.fin.news.api.dto.GoogleFinanceCrawlResponse;
import com.finsight.fin.news.config.GoogleNewsProperties;
import com.finsight.fin.news.crawl.GoogleNewsBrowserClient;
import com.finsight.fin.news.crawl.GoogleNewsCard;
import com.finsight.fin.news.crawl.GoogleNewsRssClient;
import com.finsight.fin.news.crawl.GoogleNewsWebDriverFactory;
import com.finsight.fin.news.domain.NewsArticle;
import com.finsight.fin.news.repository.NewsArticleRepository;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class GoogleFinanceNewsCrawlService {

    private static final String CRAWL_SOURCE = "GOOGLE_NEWS_FINANCE";
    private static final String ARTICLE_SOURCE = "generic-news-article";

    private final CrawlOperations crawlOperations;
    private final GoogleNewsProperties googleNewsProperties;
    private final GoogleNewsBrowserClient googleNewsBrowserClient;
    private final GoogleNewsRssClient googleNewsRssClient;
    private final GoogleNewsWebDriverFactory googleNewsWebDriverFactory;
    private final NewsArticleRepository newsArticleRepository;
    private final NewsUrlNormalizer newsUrlNormalizer;

    public GoogleFinanceNewsCrawlService(
            CrawlOperations crawlOperations,
            GoogleNewsProperties googleNewsProperties,
            GoogleNewsBrowserClient googleNewsBrowserClient,
            GoogleNewsRssClient googleNewsRssClient,
            GoogleNewsWebDriverFactory googleNewsWebDriverFactory,
            NewsArticleRepository newsArticleRepository,
            NewsUrlNormalizer newsUrlNormalizer
    ) {
        this.crawlOperations = crawlOperations;
        this.googleNewsProperties = googleNewsProperties;
        this.googleNewsBrowserClient = googleNewsBrowserClient;
        this.googleNewsRssClient = googleNewsRssClient;
        this.googleNewsWebDriverFactory = googleNewsWebDriverFactory;
        this.newsArticleRepository = newsArticleRepository;
        this.newsUrlNormalizer = newsUrlNormalizer;
    }

    public GoogleFinanceCrawlResponse crawl(GoogleFinanceCrawlRequest request) {
        int maxScrollCount = request.maxScrollCount() == null
                ? googleNewsProperties.getMaxScrollCount()
                : request.maxScrollCount();
        int maxArticlesPerRun = request.maxArticlesPerRun() == null
                ? googleNewsProperties.getMaxArticlesPerRun()
                : request.maxArticlesPerRun();

        int created = 0;
        int skipped = 0;
        int failed = 0;

        List<GoogleNewsCard> cards = googleNewsRssClient.collectCards(maxArticlesPerRun);

        WebDriver driver = googleNewsWebDriverFactory.create();
        try {
            for (GoogleNewsCard card : cards) {
                String originalUrl = googleNewsBrowserClient.resolveOriginalUrl(driver, card.googleNewsUrl());
                if (originalUrl == null || originalUrl.isBlank()) {
                    failed++;
                    continue;
                }

                String normalizedUrl = newsUrlNormalizer.normalize(originalUrl);
                if (newsArticleRepository.existsByNormalizedOriginalUrl(normalizedUrl)) {
                    skipped++;
                    continue;
                }

                CrawlResult crawlResult = crawlOperations.execute(
                        new CrawlRequest(
                                ARTICLE_SOURCE,
                                originalUrl,
                                Map.of(
                                        "keyword", googleNewsProperties.getKeyword(),
                                        "googleNewsUrl", card.googleNewsUrl()
                                )
                        )
                );

                if (!crawlResult.success() || crawlResult.content().isBlank()) {
                    failed++;
                    continue;
                }

                NewsArticle article = new NewsArticle();
                article.setCrawlSource(CRAWL_SOURCE);
                article.setKeyword(googleNewsProperties.getKeyword());
                article.setTitle(resolveTitle(card, crawlResult));
                article.setPublisher(resolvePublisher(card, originalUrl));
                article.setSourceHost(newsUrlNormalizer.host(originalUrl));
                article.setGoogleNewsUrl(card.googleNewsUrl());
                article.setOriginalUrl(originalUrl);
                article.setNormalizedOriginalUrl(normalizedUrl);
                article.setContent(crawlResult.content());
                article.setCrawledAt(LocalDateTime.now());
                newsArticleRepository.save(article);
                created++;
            }

            return new GoogleFinanceCrawlResponse(
                    googleNewsProperties.getKeyword(),
                    cards.size(),
                    created,
                    skipped,
                    failed
            );
        } finally {
            driver.quit();
        }
    }

    private String resolveTitle(GoogleNewsCard card, CrawlResult crawlResult) {
        if (crawlResult.title() != null && !crawlResult.title().isBlank()) {
            return crawlResult.title();
        }
        return card.title();
    }

    private String resolvePublisher(GoogleNewsCard card, String originalUrl) {
        if (card.publisher() != null && !card.publisher().isBlank()) {
            return card.publisher();
        }
        return newsUrlNormalizer.host(originalUrl);
    }
}
