package com.example.systeminfo;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class BatteryActivity extends Activity {
	static  TextView batteryLevel;
	private TextView batteryStatus;
	private ProgressBar pb;
	public static Button enableBat;
	public static Button disableBat;
	Intent batteryintent;
	Message msg;
	Messenger mService = null;
	boolean mServiceConnected = false;
	String value;
	boolean monitor = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    value = extras.getString("times");
		}
		
		setContentView(R.layout.activity_battery);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		enableBat = (Button)findViewById(R.id.enableBat);
		disableBat = (Button)findViewById(R.id.disableBat);
		disableBat.setEnabled(false);
		batteryintent = new Intent(BatteryActivity.this, BatteryService.class);
		/*if (value.equals("first"))
		{
		
		batteryintent = new Intent(BatteryActivity.this, BatteryService.class);
		
		
			
		System.out.println(Context.BIND_AUTO_CREATE);
	//	bindService(batteryintent, mConn, Context.BIND_AUTO_CREATE);
		
		}
		else
		{*/
			boolean runn = isServiceRunning("com.example.systeminfo.BatteryService");
			//batteryintent = new Intent(BatteryActivity.this, BatteryService.class);
			if(isServiceRunning("com.example.systeminfo.BatteryService"))
			{
			
		bindService(batteryintent, mConn, Context.BIND_AUTO_CREATE);
        enableBat.setEnabled(false);
        disableBat.setEnabled(true);
			}

	//	}
		
		batteryLevel = (TextView)findViewById(R.id.batteryLevel);
		batteryStatus = (TextView)findViewById(R.id.batteryStatus);
		pb = (ProgressBar) findViewById(R.id.progressbar);

		
		

		
		enableBat.setOnClickListener(new View.OnClickListener() {
			@Override
	        public void onClick(View v){
		        enableBat.setEnabled(false);
		        disableBat.setEnabled(true);
				monitor = true;
				startService(batteryintent);			
				bindService(batteryintent, mConn, Context.BIND_AUTO_CREATE);
				


	        }
		});
		
		disableBat.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				monitor = false;
				unbindService(mConn);
				stopService(batteryintent);
				batteryLevel.setText("Uknown");
				batteryStatus.setText("Unknown");
				pb.setProgress(0);
				enableBat.setEnabled(true);
				disableBat.setEnabled(false);
			}
		});
	}


	
	
	private ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
        	
	        mService = new Messenger(service);
	        mServiceConnected = true;
	        msg = Message.obtain(null,BatteryService.MESSAGE_TYPE_REGISTER);
	        msg.replyTo = mMessenger;
	        try{
	        	mService.send(msg);
	        }catch (RemoteException e){
	        	e.printStackTrace();
	        }
        }

        @Override
        public void onServiceDisconnected(ComponentName className){
	        Log.d("MessengerActivity", "Disconnected from service.");
	        mService = null;
	        mServiceConnected = false;
        }
	};
	
	
	
	
	
	
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	protected void onStart(){
		super.onStart();
    }
	
	
	
	
	@Override
	public void onStop(){
		super.onStop();
		System.out.println("ginetai unbind");
		msg = Message.obtain(null,BatteryService.MESSAGE_TYPE_TEXT);
        try{
        	mService.send(msg);
        }catch (RemoteException e){
        	e.printStackTrace();
        }
		
		if(monitor == true)
		{
		unbindService(mConn);
		}
		
		
		

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_battery_status, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg){

        	String[] parts = null;
        	if(msg.what == BatteryService.MESSAGE_TYPE_TEXT) {
        		Bundle b = msg.getData();
        		String text = null;
        		System.out.println("erxatai handler1");
        		if(b != null){
        			text = b.getString("data");
        			parts = text.split("/");
        		} 
        		else{
        			text = "Service responded with empty message";
        		}
        		Log.d("MessengerActivity", "Response: " + text);
        		
        		if(!text.equals("null/null"))
        		{
        		batteryLevel.setText(parts[0] + "%");
        		batteryStatus.setText(parts[1]);
        		pb.setProgress(Integer.parseInt(parts[0]));
        	} 
        	}
        	else{
        		super.handleMessage(msg);
        	}
        }
	}
	

	final Messenger mMessenger = new Messenger(new IncomingHandler());

	void sendToService(CharSequence text){
		if(mServiceConnected){
			Message msg = Message.obtain(null,
			BatteryService.MESSAGE_TYPE_TEXT);
			Bundle b = new Bundle();
			b.putCharSequence("data", text);
            msg.setData(b);
            try{
            	mService.send(msg);
            }catch(RemoteException e) {
            	e.printStackTrace();
            }
		} 
		else{
			Log.d("MessengerActivity", "Cannot send - not connectedto service.");
		}
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
	
	
}