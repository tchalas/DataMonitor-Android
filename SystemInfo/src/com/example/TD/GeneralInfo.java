package com.example.TD;

public class GeneralInfo {
	private String model;
	private String manufacturer;
	private String sdkVersion;

	public void setModel(String model){
		this.model = model;
	}
	
	public String getModel(){
		return this.model;
	}
	
	public void setManufacturer(String manufacturer){
		this.manufacturer = manufacturer;
	}
	
	public String getManufacturer(){
		return this.manufacturer;
	}
	
	public void setAndroidVersion(String version){
		this.sdkVersion = version;
	}
	
	public String getAndroidVersion(){
		return this.sdkVersion;
	}
	
	public String toString(){
		String append;
		append = sdkVersion + "/" + model + "/" + manufacturer;
		return append;
	}
}
