package org.contextlogger.android.sensors;

import java.util.List;
import java.util.ListIterator;
import org.contextlogger.android.DatabaseHelper;
import org.contextlogger.android.LoggerService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiReceiverNetworks extends BroadcastReceiver {
	private WifiManager wifi;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> resultList = wifi.getScanResults(); 
		ListIterator<ScanResult> results = resultList.listIterator(); 
		while (results.hasNext()) {
			ScanResult info = results.next(); 
			String wifiInfo = "Name: " + info.SSID +
			"; capabilities = " + info.capabilities + "; sig str = " + info.level + "dBm" +
					" BSSID = " + info.BSSID + "; frequency = " + info.frequency;
			Log.d("app", wifiInfo); 
			ContentValues values = new ContentValues();
			values.put(DatabaseHelper.TABLE_WIFI_NETWORKS_TIME, System.currentTimeMillis()/1000);
			values.put(DatabaseHelper.TABLE_WIFI_NETWORKS_BSSID, "" + info.BSSID);
			values.put(DatabaseHelper.TABLE_WIFI_NETWORKS_CAPABILITIES, "" + info.capabilities);
			values.put(DatabaseHelper.TABLE_WIFI_NETWORKS_FREQUENCY, "" + info.frequency);
			values.put(DatabaseHelper.TABLE_WIFI_NETWORKS_SIGNALSTRENGTH, "" + info.level);
			values.put(DatabaseHelper.TABLE_WIFI_NETWORKS_SSID, "" + info.SSID);
			LoggerService.db.insert(DatabaseHelper.TABLE_NAME_WIFI_NETWORKS, null, values);
		}
	}

}
