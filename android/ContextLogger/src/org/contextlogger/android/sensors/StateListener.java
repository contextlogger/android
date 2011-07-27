package org.contextlogger.android.sensors;

import org.contextlogger.android.DatabaseHelper;
import org.contextlogger.android.LoggerService;

import android.content.ContentValues;
import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

public class StateListener extends PhoneStateListener implements iSensor {
	@SuppressWarnings("unused")
	private Context c;
	private TelephonyManager tlf;
	public StateListener(Context c, final int whatToListenTo) {
		this.c = c;
		tlf = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
		tlf.listen(this, whatToListenTo);
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		Log.d("app", "Call state: " + state);
		if (state == TelephonyManager.CALL_STATE_RINGING){
			Log.d("app", "Ringing number: " + incomingNumber);
		}
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TABLE_CALL_STATE_TIME, System.currentTimeMillis()/1000);
		values.put(DatabaseHelper.TABLE_CALL_STATE_STATE,""+state);
		LoggerService.db.insert(DatabaseHelper.TABLE_NAME_CALL_STATE, null, values);
	}

	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
		Log.d("app", "Signal: " + signalStrength.getGsmSignalStrength());
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TABLE_SIGNAL_STRENGTH_TIME, System.currentTimeMillis()/1000);
		values.put(DatabaseHelper.TABLE_SIGNAL_STRENGTH_GSMSIGNAL,""+signalStrength.getGsmSignalStrength());
		values.put(DatabaseHelper.TABLE_SIGNAL_STRENGTH_GSMBITERRORRATE, ""+signalStrength.getGsmBitErrorRate());
		LoggerService.db.insert(DatabaseHelper.TABLE_NAME_SIGNAL_STRENGTH, null, values);
	}
	
	public void onCellLocationChanged(CellLocation location){
		GsmCellLocation loc = (GsmCellLocation)location;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TABLE_CELL_LOCATION_TIME, System.currentTimeMillis()/1000);
		values.put(DatabaseHelper.TABLE_CELL_LOCATION_CELLLOCATION,""+loc.getCid());
		LoggerService.db.insert(DatabaseHelper.TABLE_NAME_CELL_LOCATION, null, values);
		Log.d("app", "Cell location: " + loc.getLac() + " Cell id:"+loc.getCid());
	}

	@Override
	public void onCallForwardingIndicatorChanged(boolean cfi) {
		Log.d("app", "Forwarding indicator: " + cfi);
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TABLE_CALL_FORWARDING_TIME, System.currentTimeMillis()/1000);
		values.put(DatabaseHelper.TABLE_CALL_FORWARDING_VALUE, "" + cfi);
		LoggerService.db.insert(DatabaseHelper.TABLE_NAME_CALL_FORWARDING, null, values);
	}

	@Override
	public void onDataConnectionStateChanged(int state, int networkType) {
		Log.d("app", "Data connection state: "+state + " on network " + networkType);
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TABLE_DATA_CONNECTION_STATE_TIME, System.currentTimeMillis()/1000);
		values.put(DatabaseHelper.TABLE_DATA_CONNECTION_STATE_STATE, "" + state);
		values.put(DatabaseHelper.TABLE_DATA_CONNECTION_STATE_NETWORKTYPE, "" + networkType);
		LoggerService.db.insert(DatabaseHelper.TABLE_NAME_DATA_CONNECTION_STATE, null, values);
	}

	@Override
	public void onServiceStateChanged(ServiceState serviceState) {
		Log.d("app", "Service state: " + serviceState.getState() + " " + serviceState.getOperatorAlphaLong() + " " + serviceState.getOperatorNumeric());
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.TABLE_SERVICE_STATE_TIME, System.currentTimeMillis()/1000);
		values.put(DatabaseHelper.TABLE_SERVICE_STATE_VALUE, "" + serviceState.getState());
		LoggerService.db.insert(DatabaseHelper.TABLE_NAME_SERVICE_STATE, null, values);
	}

	@Override
	public void unregister() {
		tlf.listen(this, PhoneStateListener.LISTEN_NONE);
	}
	
	
	
}
