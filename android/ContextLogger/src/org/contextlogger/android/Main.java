package org.contextlogger.android;


import org.contextlogger.android.R;
import misc.ExportDatabaseFileTask;
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
		if (preferences.getBoolean(Constants.PREF_FIRST_RUN, true)){
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
    
    public void btn_export_clicked(View v){
    	new ExportDatabaseFileTask().execute(preferences.getString(Constants.PREF_UPLOAD_URL, ""), ExportDatabaseFileTask.ACTION_EXPORT);
    }
    
    @Override
	protected void onResume() {
		if (preferences.getString(Constants.PREF_UPLOAD_URL, "").equals("")){
//			disable buttons
			findViewById(R.id.btn_upload).setEnabled(false);
		} else {
//			enable buttons
			findViewById(R.id.btn_upload).setEnabled(true);
		}
		super.onResume();
	}

	public void btn_upload_clicked(View v){
		new ExportDatabaseFileTask().execute(preferences.getString(Constants.PREF_UPLOAD_URL, ""), ExportDatabaseFileTask.ACTION_UPLOAD);
    }
    
    private void onFirstRun(){
//    	set all to true as default
    	SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Constants.PREF_SIGNAL_STRENGTHS, true);
        editor.putBoolean(Constants.PREF_CALL_FORWARDING, true);
        editor.putBoolean(Constants.PREF_CALL_STATE, true);
        editor.putBoolean(Constants.PREF_CELL_LOCATION, true);
        editor.putBoolean(Constants.PREF_DATA_CONNECTION_STATE, true);
        editor.putBoolean(Constants.PREF_SERVICE_STATE, true);
        editor.putBoolean(Constants.PREF_WIFI_ONOFF, true);
        editor.putBoolean(Constants.PREF_WIFI_NETWORKS, true);
        editor.putBoolean(Constants.PREF_BT_DEVICES, true);
        editor.putInt(Constants.PREF_BT_FREQUENCY, 40);
        
        // mark that the first run has already happened
        editor.putBoolean(Constants.PREF_FIRST_RUN, false);
        editor.commit();
    }
}