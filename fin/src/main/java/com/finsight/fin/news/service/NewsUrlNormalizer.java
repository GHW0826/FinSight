package com.finsight.fin.news.service;

import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class NewsUrlNormalizer {

    public String normalize(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("url must not be blank");
        }

        try {
            URI uri = new URI(url.trim());
            String scheme = uri.getScheme() == null ? "https" : uri.getScheme().toLowerCase(Locale.ROOT);
            String host = uri.getHost() == null ? "" : uri.getHost().toLowerCase(Locale.ROOT);
            int port = uri.getPort();
            String path = uri.getPath() == null || uri.getPath().isBlank() ? "/" : uri.getPath();
            String query = normalizeQuery(uri.getQuery());

            URI normalized = new URI(
                    scheme,
                    null,
                    host,
                    port,
                    path,
                    query,
                    null
            );
            return normalized.toASCIIString();
        } catch (URISyntaxException exception) {
            throw new IllegalArgumentException("invalid url: " + url, exception);
        }
    }

    public String host(String url) {
        try {
            URI uri = new URI(url);
            return uri.getHost() == null ? "" : uri.getHost().toLowerCase(Locale.ROOT);
        } catch (URISyntaxException exception) {
            return "";
        }
    }

    private String normalizeQuery(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }

        String normalized = Arrays.stream(query.split("&"))
                .filter(part -> !part.isBlank())
                .filter(part -> !part.startsWith("utm_"))
                .filter(part -> !part.startsWith("fbclid="))
                .filter(part -> !part.startsWith("gclid="))
                .sorted()
                .collect(Collectors.joining("&"));

        return normalized.isBlank() ? null : normalized;
    }
}
