package org.contextlogger.android.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {
    private final int REQ_STARTER = 2;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.startContextLogger(); 
    }
    
    private void startContextLogger(){
    	Intent i = new Intent("org.contextlogger.action.START");  
    	startActivityForResult(i, this.REQ_STARTER);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQ_STARTER) {
			finish();
		}
	}
}