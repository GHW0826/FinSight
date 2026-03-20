package com.finsight.fin.news.crawl;

import com.finsight.crawl.api.CrawlRequest;
import com.finsight.crawl.api.CrawlResult;
import com.finsight.crawl.config.CrawlProperties;
import com.finsight.crawl.support.AbstractJsoupCrawler;
import com.finsight.crawl.support.JsoupDocumentFetcher;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class GenericNewsArticleCrawler extends AbstractJsoupCrawler {

    public GenericNewsArticleCrawler(
            JsoupDocumentFetcher documentFetcher,
            CrawlProperties crawlProperties
    ) {
        super(documentFetcher, crawlProperties.getMaxContentLength());
    }

    @Override
    public String supports() {
        return "generic-news-article";
    }

    @Override
    public CrawlResult crawl(CrawlRequest request) {
        try {
            Document document = fetch(request);
            return success(request, document.title(), extractMainText(document), 200);
        } catch (Exception exception) {
            return failure(request, -1, exception);
        }
    }
}
