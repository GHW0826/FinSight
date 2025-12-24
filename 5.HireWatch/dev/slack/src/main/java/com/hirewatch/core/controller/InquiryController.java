package com.hirewatch.core.controller;

import com.hirewatch.core.dto.InquiryRequest;
import com.hirewatch.core.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping("/inquiry")
    public void postInquiry (@RequestBody InquiryRequest inquiryRequestDto) throws IOException {
        inquiryService.postInquiry(inquiryRequestDto);
    }
}