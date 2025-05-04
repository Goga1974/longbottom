package com.goga74.platform.controller.dto.response;

public class JbackApiResponse {
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
