package com.example.systeminfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.provider.Settings.Secure;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.TD.TerminalData;

public class General_Info extends Activity {
	private TextView sdkVersion;
	private TextView brand;
	private TextView manufacturer;
	private Button sendData;
	private String androidId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_general_info);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		sdkVersion = (TextView)findViewById(R.id.sdkversion);
		brand = (TextView)findViewById(R.id.brand);
		manufacturer = (TextView)findViewById(R.id.manufacturer);
		sendData = (Button)findViewById(R.id.send);
		
		sendData.setEnabled(false);
		
		String version = android.os.Build.VERSION.RELEASE;
		String model = android.os.Build.MODEL;
		String creator = android.os.Build.MANUFACTURER;
		
		sdkVersion.setText("SDK Version is: " + version);
		brand.setText("Model brand is: " + model);
		manufacturer.setText("The Manufacturer is: " + creator);
		androidId = Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);

		Memory gs = (Memory) getApplication();
		gs.setModel(model);
		gs.setVersion(version);
		gs.setManufacturer(creator);
		
		if(isServiceRunning("com.example.systeminfo.BatteryService") && isServiceRunning("com.example.systeminfo.GpsTracker")){
			sendData.setEnabled(true);
		}
				
		sendData.setOnClickListener(new View.OnClickListener() {
			@Override
	        public void onClick(View v){
				System.out.println("pataw to sent");
				Context con = getApplicationContext();
				WebServiceT data = new WebServiceT(androidId, con);
				data.start();
	        }
		});
	}

	private boolean isServiceRunning(String serviceName) {
	    ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (serviceName.equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_general_info, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
}
