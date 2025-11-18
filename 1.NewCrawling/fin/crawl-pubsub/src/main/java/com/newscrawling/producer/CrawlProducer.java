package com.newscrawling.producer;

import com.newscrawling.common.pubsub.CrawlJobMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CrawlProducer {
    private final KafkaTemplate<String, CrawlJobMessage> kafkaTemplate;

    public CrawlProducer(KafkaTemplate<String, CrawlJobMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 간단하게: 각 site마다 page=1 job 하나씩 발행
     * 나중에 페이지/날짜 단위로 job을 쪼개려면 이 부분 확장
     */
    public void publishJobs() {
//            for (Source source : crawlProperties.getSites()) {
//                CrawlJobMessage job = new CrawlJobMessage(site, 1);
//                log.info("[STANDALONE_PUBSUB] publish job site={} page={}", site, job.getPageNo());
//                kafkaTemplate.send(KafkaTopics.CRAWL_JOB, site.name(), job);
//            }
    }
}
