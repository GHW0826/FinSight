package com.newscrawling.fin.scheduler;

import com.newscrawling.fin.config.CrawlProperties;
import com.newscrawling.fin.sevice.StandaloneDirectExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "crawl", name = "mode", havingValue = "STANDALONE_DIRECT")
public class StandaloneDirectScheduler {
    private final StandaloneDirectExecutor executor;
    private final CrawlProperties crawlProperties;

    public StandaloneDirectScheduler(StandaloneDirectExecutor executor, CrawlProperties crawlProperties) {
        this.executor = executor;
        this.crawlProperties = crawlProperties;
    }

    @Scheduled(fixedDelayString = "${crawl.interval-ms:3000}")
    public void schedule() {
        System.out.println("StandaloneDirectScheduler tick");
        // executor.runOnce();
    }
}