package com.finsight.fin.news.crawl;

import com.finsight.fin.news.config.GoogleNewsProperties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleNewsRssClient {

    private final GoogleNewsProperties googleNewsProperties;

    public GoogleNewsRssClient(GoogleNewsProperties googleNewsProperties) {
        this.googleNewsProperties = googleNewsProperties;
    }

    public List<GoogleNewsCard> collectCards(int maxArticlesPerRun) {
        try {
            Document document = Jsoup.connect(googleNewsProperties.getRssUrl())
                    .userAgent("Mozilla/5.0 (compatible; FinSightGoogleNewsRss/1.0)")
                    .timeout(googleNewsProperties.getPageLoadTimeoutSeconds() * 1000)
                    .get();

            List<GoogleNewsCard> cards = new ArrayList<>();
            for (Element item : document.select("rss > channel > item")) {
                String title = item.selectFirst("title") == null ? "" : item.selectFirst("title").text().trim();
                String googleNewsUrl = item.selectFirst("link") == null ? "" : item.selectFirst("link").text().trim();
                Element source = item.selectFirst("source");
                String publisher = source == null ? "" : source.text().trim();

                if (title.isBlank() || googleNewsUrl.isBlank()) {
                    continue;
                }

                cards.add(new GoogleNewsCard(title, publisher, googleNewsUrl));
                if (cards.size() >= maxArticlesPerRun) {
                    break;
                }
            }
            return cards;
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to fetch Google News RSS feed", exception);
        }
    }
}
