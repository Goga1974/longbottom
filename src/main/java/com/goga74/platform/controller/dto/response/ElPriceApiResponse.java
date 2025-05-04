package com.goga74.platform.controller.dto.response;

import com.goga74.platform.controller.dto.PriceEntry;

import java.util.List;

public class ElPriceApiResponse {
	private Data data;
	
	public Data getData() {
		return data;
	}
	
	public void setData(Data data) {
		this.data = data;
	}
	
	public static class Data {
		private List<PriceEntry> ee;
		
		public List<PriceEntry> getEe() {
			return ee;
		}
		
		public void setEe(List<PriceEntry> ee) {
			this.ee = ee;
		}
	}
}
