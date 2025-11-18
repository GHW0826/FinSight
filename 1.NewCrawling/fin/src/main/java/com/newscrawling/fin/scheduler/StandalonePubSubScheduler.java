package com.newscrawling.fin.scheduler;

import com.newscrawling.fin.config.CrawlProperties;
import com.newscrawling.producer.CrawlProducer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "crawl", name = "mode", havingValue = "STANDALONE_PUBSUB")
public class StandalonePubSubScheduler {

    private final CrawlProducer producer;
    private final CrawlProperties crawlProperties;

    public StandalonePubSubScheduler(CrawlProducer producer, CrawlProperties crawlProperties) {
        this.producer = producer;
        this.crawlProperties = crawlProperties;
    }

    @Scheduled(fixedDelayString = "${crawl.interval-ms:300000}")
    public void schedule() {
        producer.publishJobs();
    }
}