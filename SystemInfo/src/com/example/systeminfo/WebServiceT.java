package com.example.systeminfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;

import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import com.example.TD.TerminalData;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class WebServiceT extends Thread{
	private String SOAP_ACTION;
    private String METHOD_NAME;
    private String NAMESPACE;
    private String URL;
    private String dataToSent;
    private String imei;
    TerminalData antikeimeno;
    Context con;
    private volatile boolean running = true;
    
    public WebServiceT(String imei, Context con){ 
    	this.con = con;
    	Resources resources = con.getResources();
    	AssetManager assetManager = resources.getAssets();
    	antikeimeno = new TerminalData();
    	// Read from the /assets directory
    	try {
    	    InputStream inputStream = assetManager.open("webservices.properties");
    	    Properties properties = new Properties();
    	    properties.load(inputStream);
    	    SOAP_ACTION = properties.getProperty("pAction");
	        METHOD_NAME = properties.getProperty("pMethod");
	        NAMESPACE = properties.getProperty("pName");
	        URL = properties.getProperty("pUrl");
    	    System.out.println("The properties are now loaded");
    	}catch (IOException e) {
    	    System.err.println("Failed to open webservices property file");
    	    e.printStackTrace();
    	}
    	this.imei = imei;
    }
    
	public void run(){		
    	
    		while(running){
    			Memory toSent = Memory.getInstance();
    			antikeimeno.getGpsInfo().setLatitude(toSent.getLatitude());
    			antikeimeno.getGpsInfo().setLongtitude(toSent.getLongitude());
    			antikeimeno.getBatteryInfo().setLevel(toSent.getLevel());
    			antikeimeno.getBatteryInfo().setState(toSent.getStatus());
	    		antikeimeno.getGenInfo().setAndroidVersion(toSent.getVersion());
	    		antikeimeno.getGenInfo().setModel(toSent.getModel());
	    		antikeimeno.getGenInfo().setManufacturer(toSent.getManufacturer());
	    		String dataToSent = antikeimeno.toString();
	    		SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);	 

				PropertyInfo propInfo = new PropertyInfo();
				propInfo.name = "arg0"; 
				propInfo.setValue(imei);
				request.addProperty(propInfo);
			
				PropertyInfo propInfo1 = new PropertyInfo();
				propInfo1.name = "arg1";
				propInfo1.setValue(dataToSent);
				request.addProperty(propInfo1);
			
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.setOutputSoapObject(request);
				HttpTransportSE ht = new HttpTransportSE(URL);
				try{
					ht.call(SOAP_ACTION, envelope);	
				}catch (XmlPullParserException e ) {e.printStackTrace();}
				catch (IOException e){e.printStackTrace();}
				try{			
				    sleep(100000);
				}catch(InterruptedException e){}	 
    		}
  
  };
}
