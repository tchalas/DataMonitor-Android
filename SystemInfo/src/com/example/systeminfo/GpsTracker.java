package com.example.systeminfo;

import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;
 
public class GpsTracker extends Service implements LocationListener {
    private Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude; 
    double longitude;  
    static volatile boolean running;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    protected LocationManager locationManager;
    BroadcastReceiver fromActivity;
 
    public void onCreate(Intent intent){
    	super.onCreate();
    }
    
    @Override
    public void onStart(Intent intent, int startId){
    	Toast.makeText(getApplicationContext(), "Starting GPS monitoring", Toast.LENGTH_SHORT).show();
    	this.mContext = getApplicationContext();
    	running = true;
    	
    	final IntentFilter myFilter = new IntentFilter("sendToActivity");
    	fromActivity = new BroadcastReceiver(){
    		public void onReceive(Context context, Intent intent) {
            	String action = intent.getAction();
    			if(action.equalsIgnoreCase("sendToActivity")){
    				Intent data = new Intent();
    				getLocation();
    				data.setAction("GPSUpdate");
    				data.putExtra("Longitude", String.valueOf(longitude));
    				data.putExtra("Latitude", String.valueOf(latitude));
    				sendBroadcast(data);
    			}
    		}
		};
		this.registerReceiver(fromActivity, myFilter);
		
    	final Thread t = new Thread(){
    		public void run(){
    			while(running){
					getLocation();
					Intent data = new Intent();
					data.setAction("GPSUpdate");
					data.putExtra("Longitude", String.valueOf(longitude));
					data.putExtra("Latitude", String.valueOf(latitude));
					sendBroadcast(data);
					Memory gs = (Memory) getApplication();
					gs.setLatitude(String.valueOf(latitude));
					gs.setLongitude(String.valueOf(longitude));
	    			try{
	    				sleep(10000);
	    			}catch(InterruptedException e){e.printStackTrace();}
    			}
    		}
    	};
    	t.start();
    }
     
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	Toast.makeText(getApplicationContext(), "Stopping GPS Tracking", Toast.LENGTH_SHORT).show();
    	running = false;
    	this.unregisterReceiver(fromActivity);
    	
    }
    
    public Location getLocation(){
        try{
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled){}
            else{
            	this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            Toast.makeText(getApplicationContext(), "location.getLatitude();" , Toast.LENGTH_SHORT).show();
                            longitude = location.getLongitude();
                            Toast.makeText(getApplicationContext(), (int) location.getLatitude() , Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                
                if (isGPSEnabled) {
                    if (location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        		MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return location;
    }
    
    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GpsTracker.this);
        }
    }
 
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }
 
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }
 
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
 
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            }
        });
        alertDialog.show();
    }
 
    @Override
    public void onLocationChanged(Location location) {
    }
 
    @Override
    public void onProviderDisabled(String provider) {
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
 
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}