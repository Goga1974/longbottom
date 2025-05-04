package com.goga74.platform.DB.service;

import com.goga74.platform.DB.entity.PriceData;
import com.goga74.platform.DB.repository.PriceDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class PriceDataService {

    private final PriceDataRepository priceDataRepository;

    public PriceDataService(PriceDataRepository priceDataRepository) {
        this.priceDataRepository = priceDataRepository;
    }

    public void savePriceData(LocalDate date, String jsonData) {
        PriceData priceData = new PriceData();
        priceData.setDate(date);
        priceData.setJsonData(jsonData);
        priceDataRepository.save(priceData);
    }

    public Optional<PriceData> getPriceDataByDate(LocalDate date) {
        return priceDataRepository.findByDate(date);
    }
}

