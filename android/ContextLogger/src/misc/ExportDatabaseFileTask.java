package misc;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {
	public final static String ACTION_EXPORT = "export";
	public final static String ACTION_UPLOAD = "upload";
	
	
    // can use UI thread here
    protected void onPreExecute() {
       android.util.Log.d("app", "Exporting database...");
    }

    // automatically done on worker thread (separate from UI thread)
    protected Boolean doInBackground(final String... args) {
    	android.util.Log.d("app", "url is " + args[0]);
    	android.util.Log.d("app", "action is " + args[1]);
    	if (args[1].equals(ACTION_EXPORT)){
    		return exportFile();
    	} else if (args[1].equals(ACTION_UPLOAD)) {
    		return uploadFile(args[0]);
    	}
    	return false;
    }

    private boolean exportFile(){
    	File dbFile =
            new File(Environment.getDataDirectory() + "/data/com.contextlogger.android/databases/CL_database.db");

	   File exportDir = new File(Environment.getExternalStorageDirectory(), "");
	   if (!exportDir.exists()) {
	      exportDir.mkdirs();
	   }
	   File outputFile = new File(exportDir, dbFile.getName());
	
	   try {
	      outputFile.createNewFile();
	      this.copyFile(dbFile, outputFile);
	      return true;
	   } catch (IOException e) {
	      Log.e("app", e.getMessage(), e);
	      return false;
	   }
    }
    
    private boolean uploadFile(String urlToUse){
    	String Boundary = "--7d021a37605f0";
    	URL url;
    	File dbFile =
            new File(Environment.getDataDirectory() + "/data/com.contextlogger.android/databases/CL_database.db");
    	try {
			url = new URL(urlToUse);
			HttpURLConnection theUrlConnection = (HttpURLConnection) url.openConnection();
	        theUrlConnection.setDoOutput(true);
	        theUrlConnection.setDoInput(true);
	        theUrlConnection.setUseCaches(false);
	        theUrlConnection.setChunkedStreamingMode(1024);
	        theUrlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary="
	                + Boundary);

	        DataOutputStream httpOut = new DataOutputStream(theUrlConnection.getOutputStream());
	        
	        String str = "--" + Boundary + "\r\n"
            + "Content-Disposition: form-data;name=\"logdata\"; filename=\"" + dbFile.getName() + "\"\r\n"
            + "Content-Type: text/plain\r\n"
            + "\r\n";

	        httpOut.write(str.getBytes());

	        FileInputStream uploadFileReader = new FileInputStream(dbFile);
	        int numBytesToRead = 1024;
	        int availableBytesToRead;
	        while ((availableBytesToRead = uploadFileReader.available()) > 0)
	        {
	        	byte[] bufferBytesRead;
	        	bufferBytesRead = availableBytesToRead >= numBytesToRead ? new byte[numBytesToRead]
	        	                                                                    : new byte[availableBytesToRead];
	        	uploadFileReader.read(bufferBytesRead);
	        	httpOut.write(bufferBytesRead);
	        	httpOut.flush();
	        }
	        httpOut.write(("--" + Boundary + "--\r\n").getBytes());
	        httpOut.write(("--" + Boundary + "--\r\n").getBytes());
	        httpOut.flush();
	        httpOut.close();

	        // read & parse the response
	        InputStream is = theUrlConnection.getInputStream();
	        StringBuilder response = new StringBuilder();
	        byte[] respBuffer = new byte[4096];
	        while (is.read(respBuffer) >= 0)
	        {
	            response.append(new String(respBuffer).trim());
	        }
	        is.close();
	        android.util.Log.d("app", response.toString());
	        return true;
		} catch (MalformedURLException e){
			android.util.Log.d("app", "malformed url");
		}
    	catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
        
    }
    
    // can use UI thread here
    protected void onPostExecute(final Boolean success) {
    	if (success) {
          android.util.Log.d("app", "Export successful!");
       } else {
    	   android.util.Log.d("app", "Export failed!");
       }
    }

    void copyFile(File src, File dst) throws IOException {
       FileChannel inChannel = new FileInputStream(src).getChannel();
       FileChannel outChannel = new FileOutputStream(dst).getChannel();
       try {
          inChannel.transferTo(0, inChannel.size(), outChannel);
       } finally {
          if (inChannel != null)
             inChannel.close();
          if (outChannel != null)
             outChannel.close();
       }
    }

 }