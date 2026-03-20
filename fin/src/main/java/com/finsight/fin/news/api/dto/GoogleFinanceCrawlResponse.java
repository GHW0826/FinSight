package com.finsight.fin.news.api.dto;

public record GoogleFinanceCrawlResponse(
        String keyword,
        int collected,
        int created,
        int skipped,
        int failed
) {
}
