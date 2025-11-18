package com.newscrawling.core.controller;

import com.newscrawling.common.dto.CrawlRequest;
import com.newscrawling.common.dto.CrawlResult;
import com.newscrawling.core.service.CrawlingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crawl")
public class CrawlingController {
    private final CrawlingService crawlingService;

    public CrawlingController(CrawlingService crawlingService) {
        this.crawlingService = crawlingService;
    }

    @PostMapping
    public CrawlResult crawl(@RequestBody CrawlRequest req) {
        return crawlingService.crawl(req.getUrl());
    }
}
