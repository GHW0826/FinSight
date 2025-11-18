package com.newscrawling.common.pubsub;

import com.newscrawling.common.enums.Source;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CrawlJobMessage {
    private Source source;
    private LocalDateTime createdAt;

    public CrawlJobMessage() {
    }

    public CrawlJobMessage(Source source, int pageNo) {
        this.source = source;
        this.createdAt = LocalDateTime.now();
    }
}