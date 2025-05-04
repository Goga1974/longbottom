package com.goga74.platform.controller;

import com.goga74.platform.DB.entity.WebRequestEntity;
import com.goga74.platform.DB.repository.WebRequestRepository;
import com.goga74.platform.service.WebParsingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api")
public class WebParserController {

    private final WebParsingService webParsingService;
    private final WebRequestRepository requestRepository;

    public static final long K100 = 100 * 1024;
    public static final long K500 = 500 * 1024;

    @Autowired
    public WebParserController(WebParsingService webParsingService, WebRequestRepository requestRepository) {
        this.webParsingService = webParsingService;
        this.requestRepository = requestRepository;
    }

    @PostMapping("/parse")
    @Transactional
    public ResponseEntity<String> parseUrl(@RequestBody Map<String, String> request)
    {
        String url = request.get("url");
        if (url == null || url.isEmpty()) {
            return ResponseEntity.badRequest().body("URL is missing");
        }

        try {
            webParsingService.setUrl(url);
            String content = webParsingService.getContent();

            if (content != null) {
                long contentSize = content.getBytes(StandardCharsets.UTF_8).length;

                // Проверка размера текста без HTML-тегов
                if (contentSize < K500) {
                    requestRepository.deleteByUrl(url);

                    WebRequestEntity requestEntity = new WebRequestEntity();
                    requestEntity.setUrl(url);

                    ZonedDateTime gmtTime = ZonedDateTime.now(ZoneId.of("GMT"));
                    LocalDateTime localDateTimeInGMT = gmtTime.toLocalDateTime();
                    requestEntity.setRequestTime(localDateTimeInGMT);

                    requestEntity.setCrc(webParsingService.getCrc());
                    requestEntity.setTitle(webParsingService.getTitle());
                    requestEntity.setSize(webParsingService.getPageSize());

                    if (contentSize < K100) {
                        requestEntity.setContent(content);
                    }
                    requestRepository.save(requestEntity);

                    return ResponseEntity.ok("Request processed successfully");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content size is null");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the URL: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Content size is too large");
    }

}