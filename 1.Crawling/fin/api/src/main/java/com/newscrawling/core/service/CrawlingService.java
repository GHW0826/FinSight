package com.newscrawling.core.service;

import com.newscrawling.common.dto.CrawlResult;
import com.newscrawling.crawl.Crawler;
import com.newscrawling.crawl.JsoupCrawler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CrawlingService {

    private final Crawler jsoupCrawler;

    public CrawlingService(Crawler jsoupCrawler) {
        this.jsoupCrawler = jsoupCrawler;
    }

    public CrawlResult crawl(String url) {
        return jsoupCrawler.crawl(url);
    }
}