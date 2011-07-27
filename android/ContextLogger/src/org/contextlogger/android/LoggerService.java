package org.contextlogger.android;

import org.contextlogger.android.sensors.BatteryReceiver;
import org.contextlogger.android.sensors.HeadsetReceiver;
import org.contextlogger.android.sensors.LightSensorReceiver;
import org.contextlogger.android.sensors.StateListener;
import org.contextlogger.android.sensors.WifiReceiver;
import org.contextlogger.android.sensors.iSensor;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;

public class LoggerService extends Service {
	public static final String LOGGER = "org.contextlogger.android.LoggerService.SERVICE";
	public static final String PREFERENCES = "org.contextlogger.android.preferences";
	private static DatabaseHelper databaseHelper;
	public static SQLiteDatabase db;
	private boolean isRunning = false;
	private BatteryReceiver batteryReceiver;
	private WifiReceiver wifiReceiver;
	private StateListener sl_signalStrengths, sl_cellLocation, sl_callState, sl_callForwarding, sl_dataConnection, sl_serviceState;
	private LightSensorReceiver lightSensor;
	private HeadsetReceiver headsetReceiver;
	private SharedPreferences preferences;
	private IRemoteLogger.Stub remoteInterfaceBinder = new IRemoteLogger.Stub() {
		
		@Override
		public void updateSensorsToRecord() throws RemoteException {
			updateSensorRegistrations();
		}

		@Override
		public boolean isRunning() throws RemoteException {
			return isRunning;
		}

		@Override
		public void stopService() throws RemoteException {
			isRunning = false;
			clearAllListeners();
			stopSelf();
			
		}
	};
	
	@Override
	public void onCreate() {
		super.onCreate();
        preferences = getSharedPreferences(LoggerService.PREFERENCES, MODE_WORLD_WRITEABLE);
        databaseHelper = new DatabaseHelper(this.getApplicationContext(), null);
        db = databaseHelper.getWritableDatabase();
	}

	@Override
	public void onDestroy() {
		isRunning = false;
		// unregister receivers
		clearAllListeners();
		
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		// nothing else needs to be done here as since past Android 1.6
		// the onStartCommand is called and onStart is deprecated
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		isRunning = true;
		// Add sensors according to preferences
		updateSensorRegistrations();		
        return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return remoteInterfaceBinder;
	}
	
	private void clearReceiver(iSensor br){
		if (br != null){
			try {
				br.unregister();
			} catch (IllegalArgumentException e){
//				work around for not being able to detect if receiver is registered
			}
			br = null;
		}
	}
	
	private void clearAllListeners(){
		clearReceiver(sl_callForwarding);
		clearReceiver(sl_callState);
		clearReceiver(sl_cellLocation);
		clearReceiver(sl_dataConnection);
		clearReceiver(sl_serviceState);
		clearReceiver(sl_signalStrengths);
		
		if (wifiReceiver != null){
			try {
				unregisterReceiver(wifiReceiver);
			} catch (IllegalArgumentException e){
				// TODO replace with something else if future APIs start supporting this
				// do nothing, this is just a work around the lack for an api to check if the receiver is registered
			}
		}
		
		clearReceiver(batteryReceiver);
		clearReceiver(headsetReceiver);
		clearReceiver(lightSensor);
	}
	
	private void updateSensorRegistrations(){
		if (isRunning){
			if (preferences.getBoolean(Constants.PREF_SIGNAL_STRENGTHS, false)){
				sl_signalStrengths = new StateListener(this, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
			} else {
				clearReceiver(sl_signalStrengths);
			}
			if (preferences.getBoolean(Constants.PREF_CELL_LOCATION, false)){
				sl_cellLocation = new StateListener(this, PhoneStateListener.LISTEN_CELL_LOCATION);
			} else {  
				clearReceiver(sl_cellLocation);
			}
			if (preferences.getBoolean(Constants.PREF_CALL_STATE, false)){
				sl_callState = new StateListener(this, PhoneStateListener.LISTEN_CALL_STATE);
			} else {
				clearReceiver(sl_callState);
			}
			if (preferences.getBoolean(Constants.PREF_CALL_FORWARDING, false)){
				sl_callForwarding = new StateListener(this, PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR);
			} else {
				clearReceiver(sl_callForwarding);
			}
			if (preferences.getBoolean(Constants.PREF_DATA_CONNECTION_STATE, false)){
				sl_dataConnection = new StateListener(this, PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
			} else {
				clearReceiver(sl_dataConnection);
			}
			if (preferences.getBoolean(Constants.PREF_SERVICE_STATE, false)){
				sl_serviceState = new StateListener(this, PhoneStateListener.LISTEN_SERVICE_STATE);
			} else {
				clearReceiver(sl_serviceState);
			}
			if (preferences.getBoolean(Constants.PREF_WIFI_ONOFF, false)){
	        	wifiReceiver = new WifiReceiver(this);
	        } else {
	        	clearReceiver(wifiReceiver);
	        }
			
			if (preferences.getBoolean(Constants.PREF_BATTERY, false)){
	//        	instantiate and register receiver
				batteryReceiver = new BatteryReceiver(this);
	        } else {
	    		clearReceiver(batteryReceiver);
	        }
			
			if (preferences.getBoolean(Constants.PREF_LIGHT_SENSOR, false)){
	    		lightSensor = new LightSensorReceiver(this);
	        } else {
	    		clearReceiver(lightSensor);
	        }
			
			if (preferences.getBoolean(Constants.PREF_HEADSET_EVENTS, false)){
	        	if (headsetReceiver == null)
	        		headsetReceiver = new HeadsetReceiver(this);
	        } else {
	        	clearReceiver(headsetReceiver);
	        }
		} 
	}
}
