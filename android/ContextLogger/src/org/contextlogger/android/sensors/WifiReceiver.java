package org.contextlogger.android.sensors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiver extends BroadcastReceiver {
	private Context context;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		Log.d("app", "Wifi state is: " + intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1));  
	}

}
