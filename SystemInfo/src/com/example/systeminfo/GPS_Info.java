package com.example.systeminfo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GPS_Info extends Activity{
	private TextView latitude;
	private TextView longitude;
	public static Button enable;
	private static Button disable;
	private BroadcastReceiver GpsReceiver;
	private Memory gs;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gps_info);
		System.out.println("Created");
		latitude = (TextView) findViewById(R.id.TextView02);
		longitude = (TextView) findViewById(R.id.TextView04);
		enable = (Button) findViewById(R.id.enable);
		disable = (Button) findViewById(R.id.disable);
		disable.setEnabled(false);
		gs = (Memory) getApplication();
		
		enable.setOnClickListener(new View.OnClickListener() {
			@Override
	        public void onClick(View v) {
		        Intent intent = new Intent(GPS_Info.this, GpsTracker.class);
		        startService(intent);
		        enable.setEnabled(false);
		        disable.setEnabled(true);
	        }
		});
		
		disable.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				Intent intent = new Intent(GPS_Info.this, GpsTracker.class);
				stopService(intent);
				enable.setEnabled(true);
				disable.setEnabled(false);
				latitude.setText("0.0");
				longitude.setText("0.0");
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_gps_info, menu);
		return true;
	}

	@Override
	protected void onResume(){
		super.onResume();
		Intent sent = new Intent();
		sent.setAction("sendToActivity");
		sendBroadcast(sent);
		System.out.println("esteila sto gps na mu pei to location");
		
		IntentFilter filter= new IntentFilter("GPSUpdate");
		GpsReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent){
				String action = intent.getAction();
				if(action.equalsIgnoreCase("GPSUpdate")){
					String lat = intent.getStringExtra("Latitude");
					String lon = intent.getStringExtra("Longitude");
					latitude.setText(lat);
					longitude.setText(lon);
				}
			}
		};
		
		this.registerReceiver(GpsReceiver, filter);
		//Sti periptwsi pou to broadcast esteile alla to activity itan down, otan ksanaerthei mprosta tha diavasei
		//tis nees metriseis apo tin koini mnimi
		latitude.setText(gs.getLatitude());
		longitude.setText(gs.getLongitude());
	}
	
	protected void onPause(){
		super.onPause();
		System.out.println("ela");
		this.unregisterReceiver(this.GpsReceiver);
	}
	
	@Override
	public void onBackPressed(){
		Intent intent = new Intent(this, MainActivity.class);
	    startActivity(intent); 
	}
}
