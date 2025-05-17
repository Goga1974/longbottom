package com.goga74.platform.service;

import com.goga74.platform.controller.dto.elprice.PriceEntry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElPriceEnrichmentService {

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	public static final String DEFAULT_TIMEZONE = "Europe/Tallinn";

	@Value("${my.timezone:Europe/Tallinn}")
	private String timezone;

	public List<PriceEntry> enrichPrices(List<PriceEntry> prices, boolean highlightCurrentInterval) {
		if (prices == null) {
			PriceEntry emptyEntry = new PriceEntry();
			emptyEntry.setTimestamp("0");
			emptyEntry.setPrice(0.0);
			return List.of(emptyEntry);
		}

		double minPrice = prices.stream()
				.filter(price -> price != null)
				.mapToDouble(PriceEntry::getPrice)
				.min()
				.orElse(Double.MAX_VALUE);

		LocalTime currentHour = highlightCurrentInterval ? LocalTime.now(getTimeZone()) : null;

		return prices.stream()
				.filter(price -> price != null)
				.map(price -> {
					try {
						long unixTimestamp = Long.parseLong(price.getTimestamp());
						LocalDateTime dateTime = Instant.ofEpochSecond(unixTimestamp)
								.atZone(getTimeZone()) // Используем часовой пояс
								.toLocalDateTime();

						// ToDO: use time converter to get hours!
						LocalTime time = dateTime.toLocalTime();
						price.setInterval(time.format(TIME_FORMATTER) + " - " +
								time.plusHours(1).format(TIME_FORMATTER));

						price.setMinPrice(price.getPrice() == minPrice);
						price.setCurrentInterval(highlightCurrentInterval &&
								currentHour != null &&
								currentHour.getHour() == time.getHour());
					} catch (NumberFormatException e) {
						System.err.println("Invalid timestamp format: " + price.getTimestamp());
						price.setInterval("Unknown");
						price.setMinPrice(false);
						price.setCurrentInterval(false);
					}
					return price;
				})
				.collect(Collectors.toList());
	}

	public ZoneId getTimeZone() {
		try {
			return ZoneId.of(timezone != null ? timezone : DEFAULT_TIMEZONE);
		} catch (Exception e) {
			System.err.println("Invalid timezone configuration: " + timezone);
			return ZoneId.of(DEFAULT_TIMEZONE);
		}
	}
}
