package com.finsight.fin.news.crawl;

import com.finsight.fin.news.config.GoogleNewsProperties;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.springframework.stereotype.Component;

@Component
public class GoogleNewsWebDriverFactory {

    private final GoogleNewsProperties googleNewsProperties;

    public GoogleNewsWebDriverFactory(GoogleNewsProperties googleNewsProperties) {
        this.googleNewsProperties = googleNewsProperties;
    }

    public WebDriver create() {
        try {
            return new EdgeDriver(buildEdgeOptions());
        } catch (WebDriverException edgeException) {
            return new ChromeDriver(buildChromeOptions());
        }
    }

    private EdgeOptions buildEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1600,2400");
        options.addArguments("--lang=ko-KR");
        options.addArguments("--disable-blink-features=AutomationControlled");
        if (googleNewsProperties.isHeadless()) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    private ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1600,2400");
        options.addArguments("--lang=ko-KR");
        options.addArguments("--disable-blink-features=AutomationControlled");
        if (googleNewsProperties.isHeadless()) {
            options.addArguments("--headless=new");
        }
        return options;
    }
}
