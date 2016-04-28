package com.example.TD;

public class GpsInfo{
	private String latitude;
	private String longitude;
	

	public void setLatitude(String latitude){
		this.latitude = latitude;
	}

	public String getLatitude(){
		return this.latitude;
	}
	
	public void setLongtitude(String longitude){
		this.longitude = longitude;
	}
	
	public String getLongitude(){
		return this.longitude;
	}
	
	public String toString(){
		String append;
		append = latitude + "/" + longitude;
		return append;
	}
}