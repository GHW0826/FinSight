package com.newscrawling.fin.sevice;

import com.newscrawling.common.dto.CrawlResult;
import com.newscrawling.common.enums.Source;
import com.newscrawling.crawl.service.CrawlingExecute;
import com.newscrawling.fin.config.CrawlProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "crawl", name = "mode", havingValue = "STANDALONE_DIRECT")
public class StandaloneDirectExecutor {

    private final CrawlingExecute crawlingExecute;
    private final CrawlProperties crawlProperties;
    // private final NewsArticleRepository newsArticleRepository; // 나중에 DB 넣을 때 주입

    public StandaloneDirectExecutor(CrawlingExecute crawlingExecute, CrawlProperties crawlProperties) {
        this.crawlingExecute = crawlingExecute;
        this.crawlProperties = crawlProperties;
    }

    public void runOnce() {
        // 간단히 모든 site에 대해 1페이지만 크롤링
        for (Source source : crawlProperties.getSources()) {
            CrawlResult res = crawlingExecute.crawl(Source.NAVER);
            // newsArticleRepository.saveAll(articles);
        }
    }
}