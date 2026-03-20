package com.finsight.fin.news.service;

import com.finsight.fin.news.api.dto.NewsArticleCreateRequest;
import com.finsight.fin.news.api.dto.NewsArticleResponse;
import com.finsight.fin.news.api.dto.NewsArticleUpdateRequest;
import com.finsight.fin.news.domain.NewsArticle;
import com.finsight.fin.news.repository.NewsArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional(readOnly = true)
public class NewsArticleService {

    private final NewsArticleRepository newsArticleRepository;
    private final NewsUrlNormalizer newsUrlNormalizer;

    public NewsArticleService(
            NewsArticleRepository newsArticleRepository,
            NewsUrlNormalizer newsUrlNormalizer
    ) {
        this.newsArticleRepository = newsArticleRepository;
        this.newsUrlNormalizer = newsUrlNormalizer;
    }

    public Page<NewsArticleResponse> findArticles(String keyword, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (keyword == null || keyword.isBlank()) {
            return newsArticleRepository.findAll(pageRequest).map(NewsArticleResponse::from);
        }
        return newsArticleRepository.findAllByKeywordContainingIgnoreCase(keyword, pageRequest)
                .map(NewsArticleResponse::from);
    }

    public NewsArticleResponse getArticle(Long id) {
        return NewsArticleResponse.from(findEntity(id));
    }

    @Transactional
    public NewsArticleResponse createArticle(NewsArticleCreateRequest request) {
        String normalizedUrl = newsUrlNormalizer.normalize(request.originalUrl());
        ensureUnique(normalizedUrl);

        NewsArticle article = new NewsArticle();
        article.setCrawlSource(request.crawlSource());
        article.setKeyword(request.keyword());
        article.setTitle(request.title());
        article.setPublisher(request.publisher());
        article.setSourceHost(newsUrlNormalizer.host(request.originalUrl()));
        article.setGoogleNewsUrl(request.googleNewsUrl());
        article.setOriginalUrl(request.originalUrl());
        article.setNormalizedOriginalUrl(normalizedUrl);
        article.setContent(request.content());

        return NewsArticleResponse.from(newsArticleRepository.save(article));
    }

    @Transactional
    public NewsArticleResponse updateArticle(Long id, NewsArticleUpdateRequest request) {
        NewsArticle article = findEntity(id);
        String normalizedUrl = newsUrlNormalizer.normalize(request.originalUrl());
        if (!normalizedUrl.equals(article.getNormalizedOriginalUrl())) {
            ensureUnique(normalizedUrl);
        }

        article.setTitle(request.title());
        article.setPublisher(request.publisher());
        article.setOriginalUrl(request.originalUrl());
        article.setNormalizedOriginalUrl(normalizedUrl);
        article.setSourceHost(newsUrlNormalizer.host(request.originalUrl()));
        article.setContent(request.content());

        return NewsArticleResponse.from(article);
    }

    @Transactional
    public void deleteArticle(Long id) {
        newsArticleRepository.delete(findEntity(id));
    }

    private NewsArticle findEntity(Long id) {
        return newsArticleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "news article not found"));
    }

    private void ensureUnique(String normalizedUrl) {
        if (newsArticleRepository.existsByNormalizedOriginalUrl(normalizedUrl)) {
            throw new ResponseStatusException(CONFLICT, "article with the same original url already exists");
        }
    }
}
