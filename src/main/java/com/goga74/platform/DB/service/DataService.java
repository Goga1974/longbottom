package com.goga74.platform.DB.service;

import com.goga74.platform.DB.repository.DataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DataService {

    private final DataRepository dataRepository;

    public DataService(DataRepository dataRepository)
    {
        this.dataRepository = dataRepository;
    }

    public void savePriceData(LocalDate date, String jsonData)
    {
        /*
        PriceData priceData = new PriceData();
        priceData.setDate(date);
        priceData.setJsonData(jsonData);
        dataRepository.save(priceData);
        */
    }


}

