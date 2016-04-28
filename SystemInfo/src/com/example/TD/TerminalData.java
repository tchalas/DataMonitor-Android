package com.example.TD;

public class TerminalData {
	private static BatteryInfo bat;
	private static GeneralInfo genInfo;
	private static GpsInfo	gps;
	
	public TerminalData(){
		bat = new BatteryInfo();
		genInfo = new GeneralInfo();
		gps = new GpsInfo();
	}
	
	public GeneralInfo getGenInfo(){
		return TerminalData.genInfo;
	}
	
	public BatteryInfo getBatteryInfo(){
		return TerminalData.bat;
	}
	
	public GpsInfo getGpsInfo(){
		return TerminalData.gps;
	}

	public String toString(){
		String append;
		append = gps.toString() + "/" + bat.toString() + "/" + genInfo.toString();
		return append;
	}
}
