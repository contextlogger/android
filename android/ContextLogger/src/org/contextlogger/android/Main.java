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
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity {
    private TextView lbl_status;
    private Button btn_toggle;
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
			setLabels();
		}
	};
	
	
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // connections to exterior components
		bindService(new Intent(this, LoggerService.class), connection, Context.BIND_AUTO_CREATE);
		preferences = getSharedPreferences(LoggerService.PREFERENCES, MODE_WORLD_WRITEABLE);
		
		// determine if first run and set the preferences
		if (preferences.getBoolean(getString(R.string.pref_first_run), true)){
			onFirstRun();
		}
		
		
        setContentView(R.layout.main);
        
        // Initialize gui elements
        lbl_status = (TextView)findViewById(R.id.lbl_status);
        btn_toggle = (Button)findViewById(R.id.btn_toggle);
        
    }
    
    private void setLabels(){
    	boolean isRunning = false;
		try {
			isRunning = remoteService.isRunning();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	if (isRunning){
        	lbl_status.setText(R.string.service_running);
        	btn_toggle.setText(R.string.btn_stop_service);
        } else {
        	lbl_status.setText(R.string.service_stopped);
        	btn_toggle.setText(R.string.btn_start_service);
        }
    }
    
    private void setLabels(boolean value){
    	if (value){
        	lbl_status.setText(R.string.service_running);
        	btn_toggle.setText(R.string.btn_stop_service);
        } else {
        	lbl_status.setText(R.string.service_stopped);
        	btn_toggle.setText(R.string.btn_start_service);
        }
    }
    
    public void btnToggleClicked(View v){ 
    	Intent service = new Intent(this, LoggerService.class);
    	boolean isRunning = false;
		try {
			isRunning = remoteService.isRunning();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	if (isRunning){
    		setLabels(false);
    		try {
				remoteService.stopService();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				isRunning = remoteService.isRunning();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	} else {
    		setLabels(true);
    		startService(service);
    	} 
//    	
    }
    
    public void btnEditPreferencesClicked(View v){
    	Intent preferences = new Intent(this, PreferencesActivity.class);
    	startActivity(preferences);
    }
    
    private void onFirstRun(){
//    	set all to true as default
    	SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getString(R.string.pref_signal_strenghts), true);
        editor.putBoolean(getString(R.string.pref_call_forwarding), true);
        editor.putBoolean(getString(R.string.pref_call_state), true);
        editor.putBoolean(getString(R.string.pref_cell_location), true);
        editor.putBoolean(getString(R.string.pref_data_connection_state), true);
        editor.putBoolean(getString(R.string.pref_service_state), true);
        editor.putBoolean(getString(R.string.pref_wifi_onoff), true);
        editor.putBoolean(getString(R.string.pref_wifi_networks), true);
        editor.putBoolean(getString(R.string.pref_bt_devices), true);
        editor.putInt(getString(R.string.pref_bt_frequency), 10);
        
        // mark that the first run has already happened
        editor.putBoolean(getString(R.string.pref_first_run), false);
        editor.commit();
    }
}