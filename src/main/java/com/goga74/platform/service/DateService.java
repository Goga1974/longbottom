package com.goga74.platform.service;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DateService {
	
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
	
	public String formatDate(ZonedDateTime dateTime) {
		return dateTime.format(FORMATTER);
	}
	
	public ZonedDateTime getStartOfDay(ZonedDateTime dateTime) {
		return dateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
	}
	
	public ZonedDateTime getEndOfDay(ZonedDateTime startOfDay) {
		return startOfDay.plusDays(1);
	}
}
