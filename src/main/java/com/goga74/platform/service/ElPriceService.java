package com.goga74.platform.service;

import com.goga74.platform.DB.entity.PriceData;
import com.goga74.platform.DB.service.PriceDataService;
import com.goga74.platform.controller.dto.PriceEntry;
import com.goga74.platform.controller.dto.response.ElPriceApiResponse;
import com.goga74.platform.service.cache.ElPriceCacheService;
import com.goga74.platform.util.JsonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class ElPriceService
{
	private static final Logger logger = LogManager.getLogger(ElPriceService.class);

	@Value("${elering.api.url}")
	private String apiUrl;
	
	private final DateService dateService;
	private final PriceDataService priceDataService;
	private final ElPriceEnrichmentService priceEnrichmentService;

	private final ElPriceCacheService cacheService = new ElPriceCacheService(7200);

	public ElPriceService(DateService dateService, ElPriceEnrichmentService priceEnrichmentService,
						  PriceDataService priceDataService)
	{
		this.dateService = dateService;
		this.priceEnrichmentService = priceEnrichmentService;
		this.priceDataService = priceDataService;
	}

	public List<PriceEntry> getTodayPrices()
	{
		List<PriceEntry> cachedPrices = cacheService.getCachedPrices("today");
		if (cachedPrices != null)
		{
			return cachedPrices; // Возвращаем данные из кеша, если они актуальны
		}

		LocalDate today = LocalDate.now(ZoneOffset.UTC);
		Optional<PriceData> existingPriceData = priceDataService.getPriceDataByDate(today);
		if (existingPriceData.isPresent())
		{
			// Возможно, вы захотите вернуть данные из существующей записи, если они уже есть
			// Для этого нужно будет преобразовать JSON обратно в List<PriceEntry>
			// throw new RuntimeException("Data for today is already available in the database");
	    }

		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime start = dateService.getStartOfDay(now);
		ZonedDateTime end = dateService.getEndOfDay(start);
		
		String formattedStart = dateService.formatDate(start);
		String formattedEnd = dateService.formatDate(end);
		
		RestTemplate restTemplate = new RestTemplate();
		ElPriceApiResponse response = restTemplate.getForObject(
				apiUrl + "?start=" + formattedStart + "&end=" + formattedEnd,
				ElPriceApiResponse.class
		);

		List<PriceEntry> enrichedPrices = new ArrayList<>();
		if (response != null && response.getData() != null)
		{
			enrichedPrices = priceEnrichmentService.enrichPrices(response.getData().getEe(), true);
			cacheService.updateCache("today", enrichedPrices); // Обновляем кеш новыми данными

			if (existingPriceData.isEmpty())
			{
				String jsonData = JsonUtil.convertToJsonEntries(enrichedPrices);
				priceDataService.savePriceData(today, jsonData);
			}
			return enrichedPrices;
		}
		else
		{
			logger.error("Failed to fetch data from API");
			throw new RuntimeException("Failed to fetch data from API");
		}
	}
	
	public List<PriceEntry> getTomorrowPrices()
	{
		List<PriceEntry> cachedPrices2 = cacheService.getCachedPrices("tomorrow");
		if (cachedPrices2 != null)
		{
			return cachedPrices2; // Возвращаем данные из кеша, если они актуальны
		}

		ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC).plusDays(1);
		ZonedDateTime start = dateService.getStartOfDay(now);
		ZonedDateTime end = dateService.getEndOfDay(start);
		
		String formattedStart = dateService.formatDate(start);
		String formattedEnd = dateService.formatDate(end);
		
		RestTemplate restTemplate = new RestTemplate();
		ElPriceApiResponse response = restTemplate.getForObject(
				apiUrl + "?start=" + formattedStart + "&end=" + formattedEnd,
				ElPriceApiResponse.class
		);

		List<PriceEntry> enrichedPrices2;
		if (response != null && response.getData() != null)
		{
			enrichedPrices2 = priceEnrichmentService.enrichPrices(response.getData().getEe(), false);
			cacheService.updateCache("tomorrow", enrichedPrices2); // Обновляем кеш новыми данными
			return enrichedPrices2;
		}
		else
		{
			logger.error("Failed to fetch data from API");
			throw new RuntimeException("Failed to fetch data from API");
		}
	}

}
