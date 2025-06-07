package com.goga74.platform.DB.dbservice;

import com.goga74.platform.DB.entity.elprice.PriceData;
import com.goga74.platform.DB.repository.ElPriceDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ElPriceDataService {

    private final ElPriceDataRepository elPriceDataRepository;

    public ElPriceDataService(ElPriceDataRepository elPriceDataRepository) {
        this.elPriceDataRepository = elPriceDataRepository;
    }

    public void savePriceData(LocalDate date, String jsonData) {
        PriceData priceData = new PriceData();
        priceData.setDate(date);
        priceData.setJsonData(jsonData);
        elPriceDataRepository.save(priceData);
    }

    public Optional<PriceData> getPriceDataByDate(LocalDate date) {
        return elPriceDataRepository.findByDate(date);
    }
}

