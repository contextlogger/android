package misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class ExportDatabaseFileTask extends AsyncTask<String, Void, Boolean> {

    // can use UI thread here
    protected void onPreExecute() {
       android.util.Log.d("app", "Exporting database...");
    }

    // automatically done on worker thread (separate from UI thread)
    protected Boolean doInBackground(final String... args) {

       File dbFile =
                new File(Environment.getDataDirectory() + "/data/com.contextlogger.android/databases/CL_database.db");

       File exportDir = new File(Environment.getExternalStorageDirectory(), "");
       if (!exportDir.exists()) {
          exportDir.mkdirs();
       }
       File file = new File(exportDir, dbFile.getName());

       try {
          file.createNewFile();
          this.copyFile(dbFile, file);
          return true;
       } catch (IOException e) {
          Log.e("app", e.getMessage(), e);
          return false;
       }
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