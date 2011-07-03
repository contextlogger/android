package org.contextlogger.android.sensors;

import org.contextlogger.android.DatabaseHelper;
import org.contextlogger.android.LoggerService;

import com.contextlogger.android.R;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothReceiver extends BroadcastReceiver {
	BluetoothAdapter btAdapter;
	private boolean shouldDisableAfterUse = false;
	private boolean scanOrderedByCL = false;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction(); 
		String actionText = context.getString(R.string.ACTION_START_BT_SCAN);
		if (action.equals(actionText)){
			android.util.Log.d("app", "called by alarm");
			
			btAdapter = BluetoothAdapter.getDefaultAdapter(); 
			if (btAdapter == null) {
	        	Log.d("app", "No bluetooth available.");
	        	// ... 
	    	} else {
	    		scanOrderedByCL = true;
	    		shouldDisableAfterUse = !btAdapter.isEnabled();
	    		if (!btAdapter.isEnabled()){
	    			btAdapter.enable();
	    			Log.d("app", "enabling bluetooth");
	    		} else {
	    			Log.d("app", "bluetooth enabled no need to enable from code");
	    			btAdapter.startDiscovery();
	    		}
	    	}
		} else {
			// When discovery finds a device 
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {	
				// Get the BluetoothDevice object from the Intent 
				BluetoothDevice device = intent.getParcelableExtra(
						BluetoothDevice.EXTRA_DEVICE); 
//				save data to db
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.TABLE_BT_DEVICES_TIME, System.currentTimeMillis()/1000);
				values.put(DatabaseHelper.TABLE_BT_DEVICES_NAME, device.getName());
				values.put(DatabaseHelper.TABLE_BT_DEVICES_ADDRESS, device.getAddress());
				LoggerService.db.insert(DatabaseHelper.TABLE_NAME_BT_DEVICES, null, values);
				
				Log.d("app", "BlueTooth Device found: " + device.getName() + " "
									+ device.getAddress());
			} else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {	
				Log.d("app", "discovery finished");
				if (shouldDisableAfterUse && scanOrderedByCL) {
					btAdapter.disable();
					scanOrderedByCL = false;
					Log.d("app", "Bluetooth has been disabled according to preferences");
				} else {
					Log.d("app", "Bluetooth NOT has been disabled, according to preferences");
				}
			} else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {	
				Log.d("app", "discovery started");
			} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {	
				int state = intent.getExtras().getInt(BluetoothAdapter.EXTRA_STATE);
				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.TABLE_BT_STATE_TIME, System.currentTimeMillis()/1000);
				values.put(DatabaseHelper.TABLE_BT_STATE_STATE, state);
				if (state == BluetoothAdapter.STATE_ON){
					Log.d("app", "Bluetooth radio is up");
					if (scanOrderedByCL){
						values.put(DatabaseHelper.TABLE_BT_STATE_BYUSER, 0);
						Log.d("app", "Bluetooth up by CL");
					} else {
						values.put(DatabaseHelper.TABLE_BT_STATE_BYUSER, 1);
						Log.d("app", "Bluetooth up by user");
					}
					LoggerService.db.insert(DatabaseHelper.TABLE_NAME_BT_STATE, null, values);
					btAdapter.startDiscovery();
				} else {
					Log.d("app", "Bluetooth radio is down");
				}
			}
		}
	}

}
