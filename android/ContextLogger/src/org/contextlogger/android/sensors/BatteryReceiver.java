package org.contextlogger.android.sensors;

import org.contextlogger.android.DatabaseHelper;
import org.contextlogger.android.LoggerService;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

public class BatteryReceiver extends BroadcastReceiver implements iSensor {
	private Context c;
	
	public BatteryReceiver(Context c){
		this.c = c;
		c.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}
	
	@Override
	public void onReceive(Context c, Intent i) {
//		get data from the intent
		int health = i.getExtras().getInt(BatteryManager.EXTRA_HEALTH);
		android.util.Log.d("app", "battery health is: " + health);
		int level = i.getExtras().getInt(BatteryManager.EXTRA_LEVEL);
		int plugged = i.getExtras().getInt(BatteryManager.EXTRA_PLUGGED);
		boolean present = i.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
		int scale = i.getExtras().getInt(BatteryManager.EXTRA_SCALE);
		int status = i.getExtras().getInt(BatteryManager.EXTRA_STATUS);
		String technology = i.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
		int temperature = i.getExtras().getInt(BatteryManager.EXTRA_TEMPERATURE);
		int voltage = i.getExtras().getInt(BatteryManager.EXTRA_VOLTAGE);

//		store data in db
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TABLE_BATTERY_INFO_TIME, System.currentTimeMillis()/1000);
		values.put(DatabaseHelper.TABLE_BATTERY_INFO_HEALTH, "" + health);
		values.put(DatabaseHelper.TABLE_BATTERY_INFO_LEVEL, "" + level);
		values.put(DatabaseHelper.TABLE_BATTERY_INFO_PLUGGED, "" + plugged);
		values.put(DatabaseHelper.TABLE_BATTERY_INFO_PRESENT, "" + present);
		values.put(DatabaseHelper.TABLE_BATTERY_INFO_MAXLEVEL, "" + scale);
		values.put(DatabaseHelper.TABLE_BATTERY_INFO_STATUS, "" + status);
		values.put(DatabaseHelper.TABLE_BATTERY_INFO_TECHNOLOGY, "" + technology);
		values.put(DatabaseHelper.TABLE_BATTERY_INFO_TEMPERATURE, "" + temperature);
		values.put(DatabaseHelper.TABLE_BATTERY_INFO_VOLTAGE, "" + voltage);
		LoggerService.db.insert(DatabaseHelper.TABLE_NAME_BATTERY_INFO, null, values);
	}

	@Override
	public void unregister() {
		c.unregisterReceiver(this);
	}

}
