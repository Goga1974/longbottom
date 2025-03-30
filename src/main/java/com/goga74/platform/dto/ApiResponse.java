package com.goga74.platform.dto;

import java.util.List;

public class ApiResponse {
	private Data data;
	
	public Data getData() {
		return data;
	}
	
	public void setData(Data data) {
		this.data = data;
	}
	
	public static class Data
	{
		private String id;
	}
}
