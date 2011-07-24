package org.contextlogger.android;


import org.contextlogger.android.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class PreferencesActivity extends Activity {
	private SharedPreferences preferences;
	private IRemoteLogger remoteService;
	private ServiceConnection connection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			remoteService = null;
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			remoteService = IRemoteLogger.Stub.asInterface(service);
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// connections to exterior components
		bindService(new Intent(this, LoggerService.class), connection, Context.BIND_AUTO_CREATE);
		preferences = getSharedPreferences(LoggerService.PREFERENCES, MODE_WORLD_WRITEABLE);
		
		setContentView(R.layout.preferences);
		
		EditText txt_url = (EditText)findViewById(R.id.txt_url);
		txt_url.setText(preferences.getString(Constants.PREF_UPLOAD_URL, ""));
		
		
		LinearLayout list = (LinearLayout)findViewById(R.id.lnr_listOfPreferences);
		 
		CheckBox chk;
		
		chk = new CheckBox(this);
		chk.setText(getString(R.string.chk_signal_strengths));
		chk.setChecked(preferences.getBoolean(Constants.PREF_SIGNAL_STRENGTHS, false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(Constants.PREF_SIGNAL_STRENGTHS, isChecked);
				boolean committed = editor.commit();
				while (!committed){
					committed = editor.commit();
				}
//				notifyService();
			}
		});
		
		list.addView(chk);
		
		chk = new CheckBox(this);
		chk.setText(getString(R.string.chk_wifi_onoff));
		chk.setChecked(preferences.getBoolean(Constants.PREF_WIFI_ONOFF, false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(Constants.PREF_WIFI_ONOFF, isChecked);
				boolean committed = editor.commit();
				while (!committed){
					committed = editor.commit();
				}
//				notifyService();
			}
		});
		list.addView(chk);
		
		chk = new CheckBox(this);
		chk.setText(getString(R.string.chk_call_forward_state));
		chk.setChecked(preferences.getBoolean(Constants.PREF_CALL_FORWARDING, false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(Constants.PREF_CALL_FORWARDING, isChecked);
				boolean committed = editor.commit();
				while (!committed){
					committed = editor.commit();
				}
//				notifyService();
			}
		});
		list.addView(chk);
		
		chk = new CheckBox(this);
		chk.setText(getString(R.string.chk_call_state));
		chk.setChecked(preferences.getBoolean(Constants.PREF_CALL_STATE, false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(Constants.PREF_CALL_STATE, isChecked);
				boolean committed = editor.commit();
				while (!committed){
					committed = editor.commit();
				}
//				notifyService();
			}
		});

		list.addView(chk);
		
		chk = new CheckBox(this);
		chk.setText(getString(R.string.chk_data_connection));
		chk.setChecked(preferences.getBoolean(Constants.PREF_DATA_CONNECTION_STATE, false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(Constants.PREF_DATA_CONNECTION_STATE, isChecked);
				boolean committed = editor.commit();
				while (!committed){
					committed = editor.commit();
				}
//				notifyService();
			}
		});

		list.addView(chk);
		
		chk = new CheckBox(this);
		chk.setText(getString(R.string.chk_service_status));
		chk.setChecked(preferences.getBoolean(Constants.PREF_SERVICE_STATE, false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(Constants.PREF_SERVICE_STATE, isChecked);
				boolean committed = editor.commit();
				while (!committed){
					committed = editor.commit();
				}
//				notifyService();
			}
		});

		list.addView(chk);
		
		chk = new CheckBox(this);
		chk.setText(getString(R.string.chk_cell_location));
		chk.setChecked(preferences.getBoolean(Constants.PREF_CELL_LOCATION, false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(Constants.PREF_CELL_LOCATION, isChecked);
				boolean committed = editor.commit();
				while (!committed){
					committed = editor.commit();
				}
//				notifyService();
			}
		});

		list.addView(chk);
		
		chk = new CheckBox(this);
		chk.setText(getString(R.string.chk_battery_changed));
		chk.setChecked(preferences.getBoolean(Constants.PREF_BATTERY, false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(Constants.PREF_BATTERY, isChecked);
				boolean committed = editor.commit();
				while (!committed){
					committed = editor.commit();
				}
//				notifyService();
			}
		});

		list.addView(chk);
		
		chk = new CheckBox(this);
		chk.setText(getString(R.string.chk_light_sensor));
		chk.setChecked(preferences.getBoolean(Constants.PREF_LIGHT_SENSOR, false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(Constants.PREF_LIGHT_SENSOR, isChecked);
				boolean committed = editor.commit();
				while (!committed){
					committed = editor.commit();
				}
//				notifyService();
			}
		});

		list.addView(chk);
	}
	
	@Override
	public void onBackPressed() {
//		save preference for upload URL
		EditText txt_url = (EditText)findViewById(R.id.txt_url);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(Constants.PREF_UPLOAD_URL, txt_url.getText().toString());
		editor.commit();
		
//		notify the service about changes to the settings
		notifyService();
		super.onBackPressed();
	}

	private void notifyService(){
		if (remoteService != null){
			try {
				remoteService.updateSensorsToRecord();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
    @Override
	protected void onPause() {
    	unbindService(connection);
		super.onPause();
	}

	@Override
	protected void onResume() {
		bindService(new Intent(this, LoggerService.class), connection, Context.BIND_AUTO_CREATE);
		super.onResume();
	}
}

