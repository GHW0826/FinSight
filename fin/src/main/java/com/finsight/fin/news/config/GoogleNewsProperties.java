package com.finsight.fin.news.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.news.google")
public class GoogleNewsProperties {

    private String searchUrl;
    private String rssUrl;
    private String keyword = "finance";
    private int maxScrollCount = 6;
    private long scrollPauseMillis = 1500L;
    private int maxArticlesPerRun = 30;
    private boolean headless = true;
    private int pageLoadTimeoutSeconds = 20;

    public String getSearchUrl() {
        return searchUrl;
    }

    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    public String getRssUrl() {
        return rssUrl;
    }

    public void setRssUrl(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getMaxScrollCount() {
        return maxScrollCount;
    }

    public void setMaxScrollCount(int maxScrollCount) {
        this.maxScrollCount = maxScrollCount;
    }

    public long getScrollPauseMillis() {
        return scrollPauseMillis;
    }

    public void setScrollPauseMillis(long scrollPauseMillis) {
        this.scrollPauseMillis = scrollPauseMillis;
    }

    public int getMaxArticlesPerRun() {
        return maxArticlesPerRun;
    }

    public void setMaxArticlesPerRun(int maxArticlesPerRun) {
        this.maxArticlesPerRun = maxArticlesPerRun;
    }

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public int getPageLoadTimeoutSeconds() {
        return pageLoadTimeoutSeconds;
    }

    public void setPageLoadTimeoutSeconds(int pageLoadTimeoutSeconds) {
        this.pageLoadTimeoutSeconds = pageLoadTimeoutSeconds;
    }
}
