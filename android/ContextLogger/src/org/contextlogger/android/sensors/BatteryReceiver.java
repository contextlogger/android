package org.contextlogger.android.sensors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public class BatteryReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context c, Intent i) {
		// TODO Auto-generated method stub
		int health = i.getExtras().getInt(BatteryManager.EXTRA_HEALTH);
		android.util.Log.d("app", "health is " + health);
	}

}
