package com.finsight.fin.news.api;

import com.finsight.fin.news.api.dto.GoogleFinanceCrawlRequest;
import com.finsight.fin.news.api.dto.GoogleFinanceCrawlResponse;
import com.finsight.fin.news.api.dto.NewsArticleCreateRequest;
import com.finsight.fin.news.api.dto.NewsArticleResponse;
import com.finsight.fin.news.api.dto.NewsArticleUpdateRequest;
import com.finsight.fin.news.service.GoogleFinanceNewsCrawlService;
import com.finsight.fin.news.service.NewsArticleService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NewsArticleController {

    private final NewsArticleService newsArticleService;
    private final GoogleFinanceNewsCrawlService googleFinanceNewsCrawlService;

    public NewsArticleController(
            NewsArticleService newsArticleService,
            GoogleFinanceNewsCrawlService googleFinanceNewsCrawlService
    ) {
        this.newsArticleService = newsArticleService;
        this.googleFinanceNewsCrawlService = googleFinanceNewsCrawlService;
    }

    @GetMapping("/articles")
    public Page<NewsArticleResponse> getArticles(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return newsArticleService.findArticles(keyword, page, size);
    }

    @GetMapping("/articles/{id}")
    public NewsArticleResponse getArticle(@PathVariable Long id) {
        return newsArticleService.getArticle(id);
    }

    @PostMapping("/articles")
    public NewsArticleResponse createArticle(@Valid @RequestBody NewsArticleCreateRequest request) {
        return newsArticleService.createArticle(request);
    }

    @PutMapping("/articles/{id}")
    public NewsArticleResponse updateArticle(
            @PathVariable Long id,
            @Valid @RequestBody NewsArticleUpdateRequest request
    ) {
        return newsArticleService.updateArticle(id, request);
    }

    @DeleteMapping("/articles/{id}")
    public void deleteArticle(@PathVariable Long id) {
        newsArticleService.deleteArticle(id);
    }

    @PostMapping("/crawl/google-finance")
    public GoogleFinanceCrawlResponse crawlGoogleFinance(
            @Valid @RequestBody(required = false) GoogleFinanceCrawlRequest request
    ) {
        GoogleFinanceCrawlRequest crawlRequest = request == null
                ? new GoogleFinanceCrawlRequest(null, null)
                : request;
        return googleFinanceNewsCrawlService.crawl(crawlRequest);
    }
}
