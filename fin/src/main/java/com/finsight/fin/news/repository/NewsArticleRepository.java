package com.finsight.fin.news.repository;

import com.finsight.fin.news.domain.NewsArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsArticleRepository extends JpaRepository<NewsArticle, Long> {
    boolean existsByNormalizedOriginalUrl(String normalizedOriginalUrl);

    Page<NewsArticle> findAllByKeywordContainingIgnoreCase(String keyword, Pageable pageable);
}
