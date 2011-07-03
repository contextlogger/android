package org.contextlogger.android;

import org.contextlogger.android.sensors.BluetoothReceiver;
import org.contextlogger.android.sensors.StateListener;
import org.contextlogger.android.sensors.WifiReceiver;
import org.contextlogger.android.sensors.WifiReceiverNetworks;

import com.contextlogger.android.R;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class LoggerService extends Service {
	public static final String LOGGER = "org.contextlogger.android.LoggerService.SERVICE";
	public static final String PREFERENCES = "org.contextlogger.android.preferences";
	private static DatabaseHelper databaseHelper;
	public static SQLiteDatabase db;
	private boolean isRunning = false;
	private WifiReceiver wifiReceiver;
	private WifiReceiverNetworks wifiRecNetworks;
	private TelephonyManager sourcePhoneState;
	private StateListener sl_signalStrengths, sl_cellLocation, sl_callState, sl_callForwarding, sl_dataConnection, sl_serviceState;
	private SharedPreferences preferences;
	private BluetoothReceiver btReceiver;
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
        sourcePhoneState = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
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

	private void unregisterPhoneStateReceiver(StateListener sl){
		if (sl != null){
			sourcePhoneState.listen(sl, PhoneStateListener.LISTEN_NONE);
		}
	}
	
	private void clearAllListeners(){
		unregisterPhoneStateReceiver(sl_callForwarding);
		unregisterPhoneStateReceiver(sl_callState);
		unregisterPhoneStateReceiver(sl_cellLocation);
		unregisterPhoneStateReceiver(sl_dataConnection);
		unregisterPhoneStateReceiver(sl_serviceState);
		unregisterPhoneStateReceiver(sl_signalStrengths);
		
		if (wifiReceiver != null){
			try {
				unregisterReceiver(wifiReceiver);
			} catch (IllegalArgumentException e){
				// TODO replace with something else if future APIs start supporting this
				// do nothing, this is just a work around the lack for an api to check if the receiver is registered
			}
		}
	}
	
	private void updateSensorRegistrations(){
		if (preferences.getBoolean(getString(R.string.pref_signal_strenghts), false)){
			sourcePhoneState.listen(sl_signalStrengths = new StateListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
		} else {
			if (sl_signalStrengths != null){
				sourcePhoneState.listen(sl_signalStrengths, PhoneStateListener.LISTEN_NONE);
			}
		}
		if (preferences.getBoolean(getString(R.string.pref_cell_location), false)){
			sourcePhoneState.listen(sl_cellLocation = new StateListener(), PhoneStateListener.LISTEN_CELL_LOCATION);
		} else {  
			if (sl_cellLocation != null){
				sourcePhoneState.listen(sl_cellLocation, PhoneStateListener.LISTEN_NONE);
			}
		}
		if (preferences.getBoolean(getString(R.string.pref_call_state), false)){
			sourcePhoneState.listen(sl_callState = new StateListener(), PhoneStateListener.LISTEN_CALL_STATE);
		} else {
			if (sl_callState != null){
				sourcePhoneState.listen(sl_callState, PhoneStateListener.LISTEN_NONE);
			}
		}
		if (preferences.getBoolean(getString(R.string.pref_call_forwarding), false)){
			sourcePhoneState.listen(sl_callForwarding = new StateListener(), PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR);
		} else {
			if (sl_callForwarding != null){
				sourcePhoneState.listen(sl_callForwarding, PhoneStateListener.LISTEN_NONE);
			}
		}
		if (preferences.getBoolean(getString(R.string.pref_data_connection_state), false)){
			sourcePhoneState.listen(sl_dataConnection = new StateListener(), PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
		} else {
			if (sl_dataConnection != null){
				sourcePhoneState.listen(sl_dataConnection, PhoneStateListener.LISTEN_NONE);
			}
		}
		if (preferences.getBoolean(getString(R.string.pref_service_state), false)){
			sourcePhoneState.listen(sl_serviceState = new StateListener(), PhoneStateListener.LISTEN_SERVICE_STATE);
		} else {
			if (sl_serviceState != null){
				sourcePhoneState.listen(sl_serviceState, PhoneStateListener.LISTEN_NONE);
			}
		}
		if (preferences.getBoolean(getString(R.string.pref_wifi_onoff), false)){
        	wifiReceiver = new WifiReceiver();
        	registerReceiver(wifiReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        } else {
        	if (wifiReceiver != null){
    			try {
    				unregisterReceiver(wifiReceiver);
    			} catch (IllegalArgumentException e){
    				// do nothing, this is just a work around the lack for an api to check if the receiver is registered
    			}
    		}
        }
		if (preferences.getBoolean(getString(R.string.pref_wifi_networks), false)){
    		wifiRecNetworks = new WifiReceiverNetworks();
        	registerReceiver(wifiRecNetworks, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        } else {
        	if (wifiRecNetworks != null){
    			try {
    				unregisterReceiver(wifiRecNetworks);
    			} catch (IllegalArgumentException e){
    				// do nothing, this is just a work around the lack for an api to check if the receiver is registered
    			}
    		}
        }
		
		if (preferences.getBoolean(getString(R.string.pref_bt_devices), false)){
			if (btReceiver == null) {
	    		btReceiver = new BluetoothReceiver();
	        	registerReceiver(btReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
	        	registerReceiver(btReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));
	        	registerReceiver(btReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
	        	registerReceiver(btReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
	        	registerReceiver(btReceiver, new IntentFilter("ACTION_START_BT_SCAN"));
	//        	TODO scheduler
	        	AlarmManager alm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	        	Intent i = new Intent(getString(R.string.ACTION_START_BT_SCAN));
	        	PendingIntent filter = PendingIntent.getBroadcast(this, 0, i, 0);
	        	alm.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 
	        			30000, filter);
	        	android.util.Log.d("app", "scheduled timer");
			}
        } else {
        	if (btReceiver != null){
    			try {
    				AlarmManager alm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    				Intent i = new Intent(getString(R.string.ACTION_START_BT_SCAN));
    	        	PendingIntent filter = PendingIntent.getBroadcast(this, 0, i, 0);
    				alm.cancel(filter);
    				unregisterReceiver(btReceiver);
    				btReceiver = null;
    			} catch (IllegalArgumentException e){
    				// do nothing, this is just a work around the lack for an api to check if the receiver is registered
    			}
    		}
        }
	}
}