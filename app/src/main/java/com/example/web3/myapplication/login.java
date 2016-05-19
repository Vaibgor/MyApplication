package com.example.web3.myapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.web3.myapplication.model_class.Config;
import com.example.web3.myapplication.model_class.JSONParsor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class login extends Activity {
    Button login_btn;
    EditText metUserName, metPass;
    private String vendor_code;
    JSONArray products1 = null;
    String name, imgname = null, img = null;
    SharedPreferences sharedpreferences;
    JSONParsor jsonParser = new JSONParsor();
    private static final String TAG_SUCCESS1 = "success1";
    private static final String TAG_PRODUCTS1 = "products1";
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        login_btn = (Button) findViewById(R.id.buttonLogin);
        metUserName = (EditText) findViewById(R.id.etUserName);
        metPass = (EditText) findViewById(R.id.etPss);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (metUserName.getText().toString().equals("") || metPass.getText().toString().equals("")) {
                        Toast.makeText(login.this, "Please Enter Username Or Password", Toast.LENGTH_LONG).show();
                    } else {
                        int success1;
                        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                        params2.add(new BasicNameValuePair("username", metUserName.getText().toString()));
                        params2.add(new BasicNameValuePair("password", metPass.getText().toString()));
                        JSONObject json2 = jsonParser.makeHttpRequest(Config.Login, "POST", params2);
                        Log.d("data", json2.toString());
                        if (json2.toString().equals("")) {
                            Toast.makeText(login.this, "Null..", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
                                HashMap<String, String> map;
                                success1 = json2.getInt(TAG_SUCCESS1);
                                if (success1 == 1) {
                                    products1 = json2.getJSONArray(TAG_PRODUCTS1);
                                    //   String[]  pro = new String[products1.length()];
                                    String vender_name[] = new String[products1.length()];


                                    for (int i1 = 0; i1 < products1.length(); i1++) {
                                        JSONObject c = products1.getJSONObject(i1);

                                        // String id = c.getString(TAG_PID1);
                                        name = c.getString("vendor_name");
                                        vendor_code = c.getString("vendor_code");
                                        //    SharedPreferences = getSharedPreferences(NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("vendor_code1", vendor_code);
                                        editor.apply();
                                        Toast.makeText(login.this, "welcome" + " " + name, Toast.LENGTH_LONG).show();

                                        Intent i = new Intent(login.this, Search_activity.class);
                                        i.putExtra("vendor_code", vendor_code);
                                        i.putExtra("name", name);
                                        startActivity(i);
                                    }
                                }
                                if (success1 == 0) {
                                    Toast.makeText(login.this, "Wrong Username Or Password", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                } catch (RuntimeException ex) {
                    throw ex;
                }

            }
        });

    }
}
