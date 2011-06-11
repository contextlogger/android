package org.contextlogger.android.sensors;

import org.contextlogger.android.DatabaseHelper;
import org.contextlogger.android.LoggerService;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
		Log.d("app", "Wifi state is: " + state);
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TABLE_WIFI_ONOFF_TIME, System.currentTimeMillis()/1000);
		values.put(DatabaseHelper.TABLE_WIFI_ONOFF_VALUE, "" + state);
		LoggerService.db.insert(DatabaseHelper.TABLE_NAME_WIFI_ONOFF, null, values);
	}

}
