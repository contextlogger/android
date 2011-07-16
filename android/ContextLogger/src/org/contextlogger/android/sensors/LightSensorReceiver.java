package org.contextlogger.android.sensors;

import org.contextlogger.android.DatabaseHelper;
import org.contextlogger.android.LoggerService;

import android.content.ContentValues;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightSensorReceiver implements SensorEventListener {
	SensorManager sensors;
	
	public LightSensorReceiver(Context c){
		sensors = (SensorManager)c.getSystemService(Context.SENSOR_SERVICE);
		Sensor lightSensor = sensors.getDefaultSensor(Sensor.TYPE_LIGHT);
		sensors.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	public void unregister(){
//		android.util.Log.d("app", "unregistering");
		sensors.unregisterListener(this);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
//		android.util.Log.d("app", "accuracy changed");
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
//		android.util.Log.d("app", "sensor changed, new value: " + event.values[0]);
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TABLE_LIGHT_DATA_TIME, System.currentTimeMillis()/1000);
		values.put(DatabaseHelper.TABLE_LIGHT_DATA_VALUE, "" + event.values[0]);
		LoggerService.db.insert(DatabaseHelper.TABLE_NAME_LIGHT_DATA, null, values);
	}

}
