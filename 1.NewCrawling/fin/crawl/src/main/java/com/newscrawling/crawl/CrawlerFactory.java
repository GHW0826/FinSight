package com.newscrawling.crawl;

import com.newscrawling.common.enums.Source;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class CrawlerFactory {
    private final Map<Source, Crawler> crawlerMap = new EnumMap<>(Source.class);

    public CrawlerFactory(List<Crawler> crawlers) {
        for (Crawler crawler : crawlers) {
            crawlerMap.put(crawler.getSource(), crawler);
        }
    }

    public Crawler getCrawler(Source source) {
        Crawler crawler = crawlerMap.get(source);
        if (crawler == null) {
            throw new IllegalArgumentException("Not support Source: " + source);
        }
        return crawler;
    }
}
