package org.contextlogger.android.sensors;

import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.TextView;

public class StateListener extends PhoneStateListener {
	private TextView tv;
	
	public TextView getTv() {
		return tv;
	}

	public void setTv(TextView tv) {
		this.tv = tv;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		Log.d("app", "Call state: " + state);
		if (state == TelephonyManager.CALL_STATE_RINGING){
			Log.d("app", "Ringing number: " + incomingNumber);
		}
		super.onCallStateChanged(state, incomingNumber);
	}

	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
	//	tv.setText (tv.getText() + "\nSignal: " + signalStrength.getGsmSignalStrength());
		Log.d("app", "Signal: " + signalStrength.getGsmSignalStrength());
	}
	
	public void onCellLocationChanged(CellLocation location){
		GsmCellLocation loc = (GsmCellLocation)location;
	//	tv.setText(tv.getText() + "\nCell location: " + loc.getLac());
		Log.d("app", "Cell location: " + loc.getLac());
	}

	@Override
	public void onCallForwardingIndicatorChanged(boolean cfi) {
		Log.d("app", "Forwarding indicator: " + cfi);
	}

	@Override
	public void onDataConnectionStateChanged(int state, int networkType) {
		Log.d("app", "Data connection state: "+state + " on network " + networkType);
	}

	@Override
	public void onServiceStateChanged(ServiceState serviceState) {
		Log.d("app", "Service state: " + serviceState.getState() + " " + serviceState.getOperatorAlphaLong() + " " + serviceState.getOperatorNumeric());
	}
	
	
	
}
