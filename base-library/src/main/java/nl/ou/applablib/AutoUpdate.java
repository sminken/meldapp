package nl.ou.applablib;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by bvgastel on 17-05-14.
 */
public class AutoUpdate {
    private static final String TAG = "AutoUpdate";

    private static final long PRECISION = 1*60*1000; // 1 min

    public static Date getLastModified(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod("HEAD");
            return new Date(connection.getLastModified());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getContentsOfURL(String url) {
        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            StringBuffer retval = new StringBuffer(3*1024*1024);
            char[] buffer = new char[10*1024];
            int count;
            while ((count = in.read(buffer)) != -1)
                retval.append(buffer);
            in.close();
            return retval.toString();
        } catch (Exception e) {
        }
        return null;
    }

    public static class UpdateTask extends AsyncTask<String,Integer,File> {
        Activity cxt;
        Date serverDate;
        long lastUpdatedOn;
        public UpdateTask(Activity cxt) {
            this.cxt = cxt;
        }
        boolean updateAvailable() {
            return serverDate != null && new Date(lastUpdatedOn+PRECISION).before(serverDate);
        }
        protected File doInBackground(String... urls) {
            //PackageInfo.lastUpdateTime

            File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    cxt.getPackageName() + "-update.apk");
            try {
                path.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                PackageInfo manager = cxt.getPackageManager().getPackageInfo(cxt.getPackageName(), 0);
                lastUpdatedOn = manager.lastUpdateTime;
                serverDate = getLastModified(urls[0]);
                Log.i(TAG, "server date:  " + serverDate);
                Log.i(TAG, "this install: " + new Date(lastUpdatedOn));
                if (!updateAvailable())
                    return null;
            } catch (PackageManager.NameNotFoundException e) {
            }

            if (ActivityCompat.checkSelfPermission(cxt, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || (Build.VERSION.SDK_INT >= 16 && ActivityCompat.checkSelfPermission(cxt, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                return null;

            try {
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();

                URL url = new URL(urls[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(connection.getInputStream());
                OutputStream output = new FileOutputStream(path);

                byte data[] = new byte[1024*256]; // 256 kb
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    //Log.i(TAG, "downloaded: " + total + " bytes");
                    publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                try {
                    path.delete();
                } catch (Exception e2) {
                }
                Log.e(TAG, "Well that didn't work out so well...");
                //Log.e(TAG, e.getMessage());
                e.printStackTrace();
            }
            Log.i(TAG, "download complete");
            return path.exists() ? path : null;
        }

        Toast toast = null;
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            /*
            Toast prevToast = toast;
            toast = Toast.makeText(CubiActivity.this, getString(R.string.updating) + ": " + values[0] + "%", Toast.LENGTH_LONG);
            toast.show();
            if (prevToast != null)
                prevToast.cancel();
                */

            if (toast == null) {
                toast = Toast.makeText(cxt, cxt.getString(R.string.update_please_accept, cxt.getString(R.string.app_name)), Toast.LENGTH_LONG);
                toast.show();
            }
        }

        // begin the installation by opening the resulting file
        @Override
        protected void onPostExecute(File path) {
            if (path == null) {
                if (updateAvailable() && (ActivityCompat.checkSelfPermission(cxt, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || (Build.VERSION.SDK_INT >= 16 && ActivityCompat.checkSelfPermission(cxt, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))) {
                    Toast.makeText(cxt, cxt.getString(R.string.update_need_permission, cxt.getString(R.string.app_name)), Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(cxt, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
                return;
            }

            /*
            Toast.makeText(CubiActivity.this, getString(R.string.please_accept_update), Toast.LENGTH_LONG).show();
            if (toast != null)
                toast.cancel();
                */

            Intent i = new Intent();
            i.setAction(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(path), "application/vnd.android.package-archive" );
            Log.i(TAG, "About to install new .apk");
            cxt.startActivity(i);
        }
    }

    public final static String RFC1123_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
    public final static DateFormat dateFormat = new SimpleDateFormat(RFC1123_PATTERN, Locale.US);

    public static void checkNewVersion(Activity cxt, String url) {
        //if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1)
        //    return;
        if (url == null || url.length() == 0)
            return;
        ConnectivityManager connManager = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            new UpdateTask(cxt).execute(url);
        }
    }
}
