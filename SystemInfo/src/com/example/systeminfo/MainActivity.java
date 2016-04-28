package com.example.systeminfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import com.example.TD.*;

public class MainActivity extends Activity {
	private static TerminalData buffer;		//endiamesi mnimi
	boolean first = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buffer = new TerminalData();
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void onButtonClicked(View view){
		boolean checked = ((Button) view).isPressed();
	    switch(view.getId()) {
	        case R.id.gps:
	            if (checked){
	            	Intent intent = new Intent(this, GPS_Info.class);
	            	intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	            	startActivity(intent);
	            }
	            break;
	        case R.id.battery:
	            if (checked){
	            	Toast.makeText(getApplicationContext(), "Epilogi Battery Status", Toast.LENGTH_SHORT).show();
	            	Intent intent = new Intent(MainActivity.this, BatteryActivity.class);
	            	if (first == true){
	            		intent.putExtra("times","first");
	            		first = false;
	            	}
	            	else{
	            		System.out.println(first);
	            		intent.putExtra("times","notfirst");
	            	}
	            	startActivity(intent);
	            }
	            break;
	        case R.id.geninfo:
	        	if (checked){
	        		Intent intent = new Intent(MainActivity.this, General_Info.class);
	        		startActivity(intent);
	        	}
	    }
	}
}
