package com.example.systeminfo;

import android.app.Application;

public class Memory extends Application{
	private String lat;
	private String lon;
	private int level;
	private String status;
	private String model;
	private String version;
	private String manufacturer;
	private static Memory singleton;
	
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
	}
	
	
	public static Memory getInstance(){
		return singleton;
	}
	
	
	public void setLatitude(String lat){
		this.lat = lat;
	}
	
	public void setLongitude(String lon){
		this.lon = lon;
	}
	
	public void setLevel(int level){
		this.level = level;
	}
	
	public void setStatus(String status){
		
		this.status = status;
	}
	
	public void setVersion(String version){
		this.version = version;
	}
	
	public void setModel(String model){
		this.model = model;
	}
	
	public void setManufacturer(String manufacturer){
		this.manufacturer = manufacturer;
	}
	
	public String getVersion(){
		return version;
	}
	
	public String getModel(){
		return model;
	}
	
	public String getManufacturer(){
		return manufacturer;
	}
	
	public String getLatitude(){
		return lat;
	}
	
	public String getLongitude(){
		return lon;
	}
	
	public int getLevel(){
		return level;
	}
	
	public String getStatus(){
		return status;
	}
}
