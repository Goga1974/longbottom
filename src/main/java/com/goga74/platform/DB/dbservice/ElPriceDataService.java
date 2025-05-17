package com.goga74.platform.DB.dbservice;

import com.goga74.platform.DB.entity.elprice.PriceData;
import com.goga74.platform.DB.repository.PriceDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ElPriceDataService {

    private final PriceDataRepository priceDataRepository;

    public ElPriceDataService(PriceDataRepository priceDataRepository) {
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

