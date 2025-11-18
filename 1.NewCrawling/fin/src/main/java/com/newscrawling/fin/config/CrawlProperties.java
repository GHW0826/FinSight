package com.newscrawling.fin.config;

import com.newscrawling.common.enums.CrawlMode;
import com.newscrawling.common.enums.Source;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "crawl")
public class CrawlProperties {
    private CrawlMode mode;
    private long intervalMs;
    private List<Source> sources;

    public CrawlMode getMode() { return mode; }
    public void setMode(CrawlMode mode) { this.mode = mode; }

    public long getIntervalMs() { return intervalMs; }
    public void setIntervalMs(long intervalMs) { this.intervalMs = intervalMs; }

    public List<Source> getSources() { return sources; }
    public void setSites(List<Source> sources) { this.sources = sources; }
}
