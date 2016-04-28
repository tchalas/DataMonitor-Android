package com.example.TD;

public class BatteryInfo {
	private int level;
	private String state;
	
	public void setLevel(int level){
		this.level=level;
	}

	public int getLevel(){
		return this.level;
	}

	public void setState(String state){
		this.state=state;
	}

	public String getState(){
		return this.state;
	}
	
	public String toString(){
		String append;
		append = level + "/" + state;
		return append;
	}
}