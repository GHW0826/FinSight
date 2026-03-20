package com.finsight.fin.news.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NewsUrlNormalizerTest {

    private final NewsUrlNormalizer newsUrlNormalizer = new NewsUrlNormalizer();

    @Test
    void normalizeRemovesTrackingQueryParameters() {
        String normalized = newsUrlNormalizer.normalize(
                "https://example.com/news?id=10&utm_source=test&gclid=abc#section"
        );

        assertThat(normalized).isEqualTo("https://example.com/news?id=10");
    }
}
