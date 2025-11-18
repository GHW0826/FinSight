package com.newscrawling.crawl.service;

import com.newscrawling.common.dto.CrawlResult;
import com.newscrawling.common.enums.Source;
import com.newscrawling.crawl.CrawlerFactory;
import org.springframework.stereotype.Service;

@Service
public class CrawlingExecute {

    private final CrawlerFactory crawlerFactory;

    public CrawlingExecute(CrawlerFactory crawlerFactory) {
        this.crawlerFactory = crawlerFactory;
    }

    public CrawlResult crawl(Source source) {
        var crawler = crawlerFactory.getCrawler(source);
        return crawler.crawl("https://www.google.com");
    }
}