package com.goga74.platform.controller.dto;

import java.util.Locale;
import com.google.gson.annotations.Expose;

public class PriceEntry
{
	@Expose
	private String timestamp;
	@Expose
	private Double price;
	private String sPrice;
	private String interval; // Интервал времени (например, "01:00 - 02:00")
	private boolean isMinPrice; // Флаг минимальной цены
	private boolean isCurrentInterval; // Флаг текущего интервала

	// ToDo: add hour in number format
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public PriceEntry setTimestamp(String timestamp)
	{
		this.timestamp = timestamp;
		return this;
	}
	
	public Double getPrice() {
		return price;
	}
	
	public PriceEntry setPrice(Double price)
	{
		this.price = price;
		// Форматируем цену и сохраняем её в sPrice
		this.sPrice = String.format(Locale.US, "%.2f", price);
		return this;
	}
	
	public String getInterval() {
		return interval;
	}
	
	public PriceEntry setInterval(String interval) {
		this.interval = interval;
		return this;
	}
	
	public boolean isMinPrice() {
		return isMinPrice;
	}
	
	public void setMinPrice(boolean minPrice) {
		isMinPrice = minPrice;
	}
	
	public boolean isCurrentInterval() {
		return isCurrentInterval;
	}
	
	public void setCurrentInterval(boolean currentInterval) {
		isCurrentInterval = currentInterval;
	}

	public String getPriceAsString() {
		return sPrice;
	}
}
