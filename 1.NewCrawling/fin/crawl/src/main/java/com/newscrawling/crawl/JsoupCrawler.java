package com.newscrawling.crawl;

import com.newscrawling.common.dto.CrawlResult;
import com.newscrawling.common.enums.Source;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("jsoupCrawler")
public class JsoupCrawler implements Crawler {

    private final int timeoutMillis;
    private final int maxContentLength;

    public JsoupCrawler(
            @Value("${crawler.timeout-millis:5000}") int timeoutMillis,
            @Value("${crawler.max-content-length:10000}") int maxContentLength
    ) {
        this.timeoutMillis = timeoutMillis;
        this.maxContentLength = maxContentLength;
    }

    @Override
    public Source getSource() {
        return Source.NAVER;
    }

    @Override
    public CrawlResult crawl(String url) {
        try {
            Connection.Response response = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (compatible; NewsCrawler/1.0)")
                    .referrer("https://www.google.com")
                    .timeout(timeoutMillis)
                    .followRedirects(true)
                    .execute();

            int status = response.statusCode();
            if (status != 200) {
                return new CrawlResult(url, null, null, status, false);
            }

            Document doc = response.parse();

            String title = doc.title();
            String content = extractMainText(doc);

            return new CrawlResult(url, title, content, status, true);

        } catch (IOException e) {
            return new CrawlResult(url, null, null, -1, false);
        }
    }

    protected String extractMainText(Document doc) {
        Element article = doc.selectFirst("article");

        String text;
        if (article != null) {
            text = article.text();
        } else if (doc.body() != null) {
            text = doc.body().text();
        } else {
            text = "";
        }

        if (text.length() > maxContentLength) {
            text = text.substring(0, maxContentLength);
        }

        return text;
    }
}