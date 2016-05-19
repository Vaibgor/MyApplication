package com.example.web3.myapplication.GPS;
/**
 * Created by web3 on 5/12/2016.
 */

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.web3.myapplication.DatabaseClass.DatabaseClass;
import com.example.web3.myapplication.model_class.Config;
import com.example.web3.myapplication.model_class.JSONParsor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vaibhav.pote on 2/7/2016.
 */
public class GPSTrackerService extends Service {
    DatabaseClass database;
    LocationManager lm;
    String provider;
    Location l;
    double lng;
    double lat;
    String longitude = Double.toString(lng);
    String latitude = Double.toString(lat);
    String ts;
    GPSTracker gps;

    JSONArray products1 = null;
    SharedPreferences sharedpreferences;
    JSONParsor jsonParser = new JSONParsor();
    private static final String TAG_SUCCESS1 = "success1";
    private static final String TAG_PRODUCTS1 = "products1";
    public static final String MyPREFERENCES = "MyPrefs";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // For time consuming an long tasks you can launch a new thread here...
        new SenderThread().start();
    }

    public class SenderThread extends Thread {
        SenderThread() {
        }

        public void run() {

            for (; ; ) {

                try {
                    sentGPSLocation();
                    Thread.sleep(50000);
                } catch (Exception e) {
                }
            }
        }
    }

    public void sentGPSLocation() {
        AsynchSentGPSLocation task = new AsynchSentGPSLocation();
        task.execute();
    }

    public class AsynchSentGPSLocation extends AsyncTask<Void, Void, Void> {
        String result;

        @Override
        protected Void doInBackground(Void... params) {
            database = new DatabaseClass(getApplicationContext());
           // int userId = database.getUserID();
            gps = new GPSTracker(GPSTrackerService.this);

            Calendar c1 = Calendar.getInstance();
            System.out.println("Current time => " + c1.getTime());
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
            String formattedDate = df.format(c1.getTime());
            String formattedtime = tf.format(c1.getTime());
            // check if GPS enabled
            if (gps.canGetLocation()) {

                lat = gps.getLatitude();
                lng = gps.getLongitude();
                // \n is for new line
                //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                //gps.showSettingsAlert();
            }
            int success1;
            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
            params2.add(new BasicNameValuePair("latitude",longitude));
            params2.add(new BasicNameValuePair("longitude", latitude));
            JSONObject json3 = jsonParser.makeHttpRequest(Config.GPSTrackLocation, "POST", params2);
            Log.d("data", json3.toString());
            try {
                final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map;
                success1 = json3.getInt(TAG_SUCCESS1);
                if (success1 == 1) {
                    products1 = json3.getJSONArray(TAG_PRODUCTS1);
                    String vender_name[] = new String[products1.length()];
                    for (int i1 = 0; i1 < products1.length(); i1++) {
                        JSONObject c = products1.getJSONObject(i1);
                        //    SharedPreferences = getSharedPreferences(NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                       // editor.putString("caf_no1", cafNumber);
                        editor.apply();
                        Toast.makeText(GPSTrackerService.this, "service called", Toast.LENGTH_LONG).show();
                    }
                }
                if (success1 == 0) {
                    Toast.makeText(GPSTrackerService.this, "Wrong Username Or Password", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
            return null;
        }
    }
}
