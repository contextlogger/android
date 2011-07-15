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
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
		
		LinearLayout list = (LinearLayout)findViewById(R.id.lnr_listOfPreferences);
		 
		CheckBox chk;
		
		chk = new CheckBox(this);
		chk.setText(getString(R.string.chk_signal_strengths));
		chk.setChecked(preferences.getBoolean(getString(R.string.pref_signal_strenghts), false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(getString(R.string.pref_signal_strenghts), isChecked);
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
		chk.setChecked(preferences.getBoolean(getString(R.string.pref_wifi_onoff), false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(getString(R.string.pref_wifi_onoff), isChecked);
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
		chk.setChecked(preferences.getBoolean(getString(R.string.pref_call_forwarding), false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(getString(R.string.pref_call_forwarding), isChecked);
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
		chk.setChecked(preferences.getBoolean(getString(R.string.pref_call_state), false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(getString(R.string.pref_call_state), isChecked);
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
		chk.setChecked(preferences.getBoolean(getString(R.string.pref_data_connection_state), false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(getString(R.string.pref_data_connection_state), isChecked);
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
		chk.setChecked(preferences.getBoolean(getString(R.string.pref_service_state), false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(getString(R.string.pref_service_state), isChecked);
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
		chk.setChecked(preferences.getBoolean(getString(R.string.pref_cell_location), false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(getString(R.string.pref_cell_location), isChecked);
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
		chk.setChecked(preferences.getBoolean(getString(R.string.pref_battery), false));
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				SharedPreferences.Editor editor = preferences.edit();
				editor.putBoolean(getString(R.string.pref_battery), isChecked);
				boolean committed = editor.commit();
				while (!committed){
					committed = editor.commit();
				}
//				notifyService();
			}
		});

		list.addView(chk);
		
        
	}
	
	public void btn_savePreferences_clicked(View v){
		notifyService();
		finish();
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

