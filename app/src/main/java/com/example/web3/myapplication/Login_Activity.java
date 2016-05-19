package com.example.web3.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.web3.myapplication.DatabaseClass.DatabaseClass;
import com.example.web3.myapplication.model_class.Config;
import com.example.web3.myapplication.model_class.JSONParsor;
import com.example.web3.myapplication.model_class.Tracking_Details_model;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
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

public class Login_Activity extends AppCompatActivity implements View.OnClickListener {
    DatabaseClass database;
    EditText metUserName, metPass;
    String strUserName, strPass;
    ProgressDialog nDialog;
    Button button;
    JSONParsor jsonParser = new JSONParsor();
    private static final String TAG_SUCCESS1 = "success1";
    private String vendor_code, name;
    private static final String TAG_PRODUCTS1 = "products1";
    JSONArray products6 = null, products1 = null;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        findViewById();
    }

    public void findViewById() {
        metUserName = (EditText) findViewById(R.id.etUserName);
        metPass = (EditText) findViewById(R.id.etPss);
        button = (Button) findViewById(R.id.buttonLogin);
        button.setOnClickListener(this);
    }

    public void btnLogin(View view) {
        try {
            strUserName = metUserName.getText().toString();
            strPass = metPass.getText().toString();

            if (strUserName.equals("")) {
                Toast.makeText(Login_Activity.this, "PLease enter user name", Toast.LENGTH_SHORT).show();
            } else if (strPass.equals("")) {
                Toast.makeText(Login_Activity.this, "please enter password", Toast.LENGTH_SHORT).show();
            } else {

                nDialog = new ProgressDialog(Login_Activity.this);
                nDialog.setTitle("Checking Network");
                nDialog.setMessage("Loading..");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(true);
                nDialog.show();
                AsynchLoginDetails asynchLoginDetails = new AsynchLoginDetails();
                asynchLoginDetails.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            strUserName = metUserName.getText().toString();
            strPass = metPass.getText().toString();

            if (strUserName.equals("")) {
                Toast.makeText(Login_Activity.this, "PLease enter user name", Toast.LENGTH_SHORT).show();
            } else if (strPass.equals("")) {
                Toast.makeText(Login_Activity.this, "please enter password", Toast.LENGTH_SHORT).show();
            } else {

                AsynchLoginDetails asynchLoginDetails = new AsynchLoginDetails();
                asynchLoginDetails.execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AsynchLoginDetails extends AsyncTask<Void, Void, Void> {
        String result;
        String URL = "http://192.168.1.111/tuts_rest/info.php";
        //String URL = Config.Login;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(Login_Activity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpPost request = new HttpPost(URL);
            Log.i("URL",URL);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {
                JSONStringer item = new JSONStringer().object()
                        .key("uid").value(strUserName)
                        .endObject();
                Log.i("Sending data", item.toString());
                StringEntity entity = new StringEntity(item.toString());
                request.setEntity(entity);
                // Set timeout values to
                BasicHttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is
                // established.
                // The default value is zero, that means the timeout is not
                // used.
                int timeoutConnection = 4000;
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 6000;
                HttpConnectionParams
                        .setSoTimeout(httpParameters, timeoutSocket);

                // Send request to WCF service with timeout values
                DefaultHttpClient httpClient = new DefaultHttpClient(
                        httpParameters);
                HttpResponse response = httpClient.execute(request);

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    String responseBody = EntityUtils.toString(response
                            .getEntity());

                    // Read response data into buffer
                    result = responseBody.toString();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            nDialog.dismiss();
            try {
                if (!result.equals("0")) {
                    database = new DatabaseClass(getApplicationContext());
                    database.insertLoginDetails(Integer.parseInt(result));
                    Intent intent = new Intent(getApplicationContext(), Search_activity.class);
                    startActivity(intent);
                } else {
                    nDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
