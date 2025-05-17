package com.goga74.platform.controller.response.jback;

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
