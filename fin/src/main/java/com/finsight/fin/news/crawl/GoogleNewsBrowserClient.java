package com.finsight.fin.news.crawl;

import com.finsight.fin.news.config.GoogleNewsProperties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class GoogleNewsBrowserClient {

    private static final String GOOGLE_NEWS_BASE_URL = "https://news.google.com";

    private final GoogleNewsProperties googleNewsProperties;

    public GoogleNewsBrowserClient(GoogleNewsProperties googleNewsProperties) {
        this.googleNewsProperties = googleNewsProperties;
    }

    public List<GoogleNewsCard> collectCards(WebDriver driver, int maxScrollCount, int maxArticlesPerRun) {
        driver.get(googleNewsProperties.getSearchUrl());
        waitUntilReady(driver);

        List<GoogleNewsCard> initialCards = parseCards(driver.getPageSource(), maxArticlesPerRun);
        if (!initialCards.isEmpty()) {
            return initialCards;
        }

        scroll(driver, maxScrollCount);
        List<GoogleNewsCard> cards = parseCards(driver.getPageSource(), maxArticlesPerRun);
        if (!cards.isEmpty()) {
            return cards;
        }

        throw buildPageStateException(driver.getCurrentUrl(), driver.getPageSource());
    }

    public String resolveOriginalUrl(WebDriver driver, String googleNewsUrl) {
        driver.get(googleNewsUrl);

        long deadline = System.currentTimeMillis() + (googleNewsProperties.getPageLoadTimeoutSeconds() * 1000L);
        while (System.currentTimeMillis() < deadline) {
            String currentUrl = driver.getCurrentUrl();
            if (isExternalUrl(currentUrl)) {
                return currentUrl;
            }

            String html = driver.getPageSource();
            if (hasUsefulContent(html)) {
                Document document = Jsoup.parse(html, googleNewsUrl);
                for (Element link : document.select("a[href^=http]")) {
                    String href = link.absUrl("href");
                    if (isExternalUrl(href)) {
                        return href;
                    }
                }
            }

            sleep(500L);
        }
        return null;
    }

    private void waitUntilReady(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(googleNewsProperties.getPageLoadTimeoutSeconds())
        );
        wait.until(ignored -> isDocumentReady(driver));
        wait.until(ignored -> hasUsefulContent(driver.getPageSource()));
    }

    private void scroll(WebDriver driver, int maxScrollCount) {
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        long previousHeight = readHeight(javascriptExecutor);

        for (int index = 0; index < maxScrollCount; index++) {
            javascriptExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            sleep(googleNewsProperties.getScrollPauseMillis());
            long currentHeight = readHeight(javascriptExecutor);
            if (currentHeight == previousHeight) {
                break;
            }
            previousHeight = currentHeight;
        }
    }

    private List<GoogleNewsCard> parseCards(String html, int maxArticlesPerRun) {
        Document document = Jsoup.parse(html, GOOGLE_NEWS_BASE_URL);
        Map<String, GoogleNewsCard> cards = new LinkedHashMap<>();

        for (Element anchor : document.select("article a[href^='./read/'], article a[href^='./articles/']")) {
            String title = anchor.text().trim();
            if (title.isBlank()) {
                continue;
            }

            String googleNewsUrl = anchor.absUrl("href");
            if (googleNewsUrl.isBlank() || cards.containsKey(googleNewsUrl)) {
                continue;
            }

            Element article = anchor.closest("article");
            String publisher = extractPublisher(article);
            cards.put(googleNewsUrl, new GoogleNewsCard(title, publisher, googleNewsUrl));

            if (cards.size() >= maxArticlesPerRun) {
                break;
            }
        }

        return List.copyOf(cards.values());
    }

    private boolean isDocumentReady(WebDriver driver) {
        Object value = ((JavascriptExecutor) driver).executeScript("return document.readyState;");
        return "complete".equals(value) || "interactive".equals(value);
    }

    private boolean hasUsefulContent(String html) {
        String normalized = html.toLowerCase(Locale.ROOT);
        return normalized.contains("<article")
                || normalized.contains("./read/")
                || normalized.contains("./articles/")
                || normalized.contains("unusual traffic")
                || normalized.contains("enable javascript")
                || normalized.contains("recaptcha");
    }

    private IllegalStateException buildPageStateException(String currentUrl, String html) {
        String normalized = html.toLowerCase(Locale.ROOT);

        if (currentUrl.contains("sorry") || normalized.contains("unusual traffic") || normalized.contains("recaptcha")) {
            return new IllegalStateException(
                    "Google News blocked the automated request with an anti-bot page. " +
                            "Try app.news.google.headless=false or reduce crawl frequency."
            );
        }

        if (normalized.contains("enable javascript")) {
            return new IllegalStateException(
                    "Google News returned a JavaScript/interstitial page instead of article cards."
            );
        }

        return new IllegalStateException(
                "Google News page loaded but no article cards were found. Current URL: " + currentUrl
        );
    }

    private String extractPublisher(Element article) {
        if (article == null) {
            return "";
        }

        Element publisherLink = article.selectFirst("a[href*='./publications/']");
        if (publisherLink != null) {
            return publisherLink.text().trim();
        }

        Element imageLink = article.selectFirst("a[href^='./read/'] + div, a[href^='./articles/'] + div");
        return imageLink == null ? "" : imageLink.text().trim();
    }

    private long readHeight(JavascriptExecutor javascriptExecutor) {
        Object value = javascriptExecutor.executeScript("return document.body.scrollHeight;");
        if (value instanceof Number number) {
            return number.longValue();
        }
        return 0L;
    }

    private boolean isExternalUrl(String url) {
        return url != null && !url.isBlank() && !url.contains("news.google.com");
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("browser wait interrupted", exception);
        }
    }
}
