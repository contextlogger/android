package org.contextlogger.android.sensors;

import org.contextlogger.android.DatabaseHelper;
import org.contextlogger.android.LoggerService;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class HeadsetReceiver extends BroadcastReceiver {
	Context c;
	
	public HeadsetReceiver(Context c){
		c.registerReceiver(this, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
	}
	
	@Override
	public void onReceive(Context c, Intent i) {
		int status = i.getExtras().getInt("state");
		int microphone = i.getExtras().getInt("microphone");
		String name = i.getExtras().getString("name");
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TABLE_HEADSET_EVENTS_TIME, System.currentTimeMillis()/1000);
		values.put(DatabaseHelper.TABLE_HEADSET_EVENTS_STATUS, "" + status);
		values.put(DatabaseHelper.TABLE_HEADSET_EVENTS_MICROPHONE, "" + microphone);
		values.put(DatabaseHelper.TABLE_HEADSET_EVENTS_NAME, "" + name);
		LoggerService.db.insert(DatabaseHelper.TABLE_NAME_HEADSET_EVENTS, null, values);
	}

	public void unregister() {
		c.unregisterReceiver(this);
	}

}
