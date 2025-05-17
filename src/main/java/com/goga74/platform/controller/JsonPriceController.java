package com.goga74.platform.controller;

import com.goga74.platform.controller.dto.elprice.PriceEntry;
import com.goga74.platform.service.ElPriceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JsonPriceController {

    private final ElPriceService elPriceService;

    public JsonPriceController(ElPriceService elPriceService) {
        this.elPriceService = elPriceService;
    }

    @GetMapping("/prices/json")
    public ResponseEntity<List<PriceEntry>> getTodayPricesAsJson() {
        List<PriceEntry> todayPrices = elPriceService.getTodayPrices();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        return ResponseEntity.ok()
                .headers(headers)
                .body(todayPrices);
    }

    @GetMapping("/prices/tomorrow_json")
    public ResponseEntity<List<PriceEntry>> getTomorrowPricesAsJson() {
        List<PriceEntry> tomorrowPrices = elPriceService.getTomorrowPrices();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");

        return ResponseEntity.ok()
                .headers(headers)
                .body(tomorrowPrices);
    }
}
