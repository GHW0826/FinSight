package com.newscrawling.crawl;


import com.newscrawling.common.dto.CrawlResult;
import com.newscrawling.common.enums.Source;

public interface Crawler {
    Source getSource();
    CrawlResult crawl(String url);
}