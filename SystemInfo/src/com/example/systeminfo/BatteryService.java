package com.example.systeminfo;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.os.Bundle;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.content.Context;
import android.util.Log;

	public class BatteryService extends Service {
		myBatteryReceiver receiver;
		static String level;
		static String status;
		public static final int MESSAGE_TYPE_REGISTER = 1;
	    static final int MSG_REGISTER_CLIENT = 1;
	    static final int MSG_SET_VALUE = 3;
		Messenger client;
		String mValue;
		Messenger mResponseMessenger = null;
		public static final int MESSAGE_TYPE_TEXT = 2;
		final Messenger mMessenger = new Messenger(new IncomingHandler());
		boolean conn=false;
		boolean oksend = false;
		boolean trexe = false;
		String data;
		int lev,stat; 

	@Override
	public IBinder onBind(Intent intent) {
        Log.d("MessengerService", "Binding messenger...");
        return mMessenger.getBinder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		receiver= new myBatteryReceiver();
		trexe=true;
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		SendThread st = new SendThread();
		st.start();
	}
	
	@Override
	public void onDestroy(){
		trexe = false;
		System.out.println("ginetai destroy to service");
		super.onDestroy();
		unregisterReceiver(receiver);
	}

	@Override
	public void onStart(Intent intent, int startid) {
		
	}
	
	public class myBatteryReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent){
			lev = intent.getIntExtra("level", 0);
			stat = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
			if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){
				level = Integer.toString(lev);
				switch(stat){
					case BatteryManager.BATTERY_STATUS_CHARGING:
						status = "Charging";
					case BatteryManager.BATTERY_STATUS_DISCHARGING:
						status = "Discharging";
					case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
						status = "Not Charging";
					case BatteryManager.BATTERY_STATUS_FULL:
						status = "Battery Full";
				}
			}
	}
	}
	
	
	class SendThread extends Thread
	{
		public void run()
		{
			while(trexe)
			{
				
				data = level + "/" + status;
				if(!data.equals("null/null"))
				{
						
				Memory gs = (Memory) getApplication();
				gs.setLevel(lev);
				gs.setStatus(status);
				}
		          
		           
		           System.out.println(data);
		           
		           if(oksend)
		           {
		        	   sendToActivity(data);
		           }
		        	   try {
					sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
	}
	
		
	class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
	        switch(msg.what){
		        case MESSAGE_TYPE_TEXT:
	               Bundle b = msg.getData();
	               if(b != null){
	            	   oksend=false;
	            	   System.out.println("egine false to oksend");
	                  
	               }
	               else{
	            	   System.out.println("Who's there? Speak!");
	               }
	               break;
	            case MESSAGE_TYPE_REGISTER:
	            	
	            	oksend=true;
		           Log.d("MessengerService", "Registered Activity's Messenger.");
		           mResponseMessenger = msg.replyTo;
		           String data = level + "/" + status;
		           
		           System.out.println(data);
		           sendToActivity(data);
		           break;
                   default:
                	   super.handleMessage(msg);
                   }
        }
	}
		
	void sendToActivity(String toActivity) {
        if (mResponseMessenger == null) {
        	Log.d("MessengerService", "Cannot send message to activity - no activity registered to this service.");
        } 
        else{
        	Log.d("MessengerService", "Sending message to activity: " + toActivity);
            Bundle data = new Bundle();
            data.putCharSequence("data", toActivity);
            Message msg = Message.obtain(null, MESSAGE_TYPE_TEXT);
            msg.setData(data);
            try{
            	mResponseMessenger.send(msg);
            }catch (RemoteException e) {
            	e.printStackTrace();
             }
        }
	}	
}