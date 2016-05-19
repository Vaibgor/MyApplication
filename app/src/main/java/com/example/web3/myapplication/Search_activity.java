package com.example.web3.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.web3.myapplication.DatabaseClass.AndroidDatabaseManager;
import com.example.web3.myapplication.DatabaseClass.DatabaseClass;
import com.example.web3.myapplication.model_class.Config;
import com.example.web3.myapplication.model_class.JSONParsor;
import com.example.web3.myapplication.model_class.Ofr_Assign_model;
import com.example.web3.myapplication.model_class.Tracking_Details_model;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Search_activity extends AppCompatActivity {
    DatabaseClass database;
    EditText etCAF, etDEL;
    AutoCompleteTextView autoCAFnumber;
    LinearLayout linearLayout;
    Button btnSearch;
    TextView tvFromDate, tvToDate;
    //alertDialog fields
    ImageView dateFrom, dateTo;
    String strCAF, strDEL, strFromDate, strToDate;
    CharSequence charAutoSearch;
    String cafNumber, cust_name, sub_date, fnl_status, fnl_rating, fnl_remark;
    ListView lvByDate;
    Context context;
    ArrayList<Tracking_Details_model> arrayList = new ArrayList<Tracking_Details_model>();
    AlertDialog dialog;
    ProgressDialog nDialog;
    private String[] strCAFNo = {"8811001715707", "8811001722642", "8811001750487", "8811001754856", "8811001774049", "8811001777352", "8811001777355", "8811001860326"};
    protected CustomListAdapter customListAdapter;
    ArrayAdapter<String> cafAdapter;
    private String dealer_code, dealer_name;
    JSONArray products1 = null;
    SharedPreferences sharedpreferences;
    JSONParsor jsonParser = new JSONParsor();
    private static final String TAG_SUCCESS1 = "success1";
    private static final String TAG_PRODUCTS1 = "products1";
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedpreferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bundle bundle = getIntent().getExtras();
        dealer_code = bundle.getString("vendor_code");
        dealer_name = bundle.getString("name");
        findViewByID();

        lvByDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Tracking_Details_model details_model = (Tracking_Details_model) parent.getItemAtPosition(position);

                    final String strcaf, strcust, strdate, strstatus, strrating, strremark;
                    Log.i("POSITION", String.valueOf(details_model));
                    strcaf = String.valueOf(details_model.getCaf_number());
                    Log.i("STRCAF", strcaf);
                    strcust = String.valueOf(details_model.getCust_name());
                    strdate = String.valueOf(details_model.getDate());
                    strstatus = String.valueOf(details_model.getStatus());
                    strrating = String.valueOf(details_model.getRating());
                    strremark = String.valueOf(details_model.getRemark());
                    insertDialogData(strcaf, strcust, strdate, strstatus, strrating, strremark);

                    Log.i("CONVERT", String.valueOf(details_model));
                    //final Tracking_Details_model details_model = (Tracking_Details_model) o;
                    Log.i("CAF On position", details_model.getCaf_number());
                    String CAF_POSITION = String.valueOf(details_model.getCaf_number());
                    Log.i("CAF_POSITION", CAF_POSITION);
                    //Toast.makeText(Search_activity.this, "youClick" + o, Toast.LENGTH_SHORT).show();
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void findViewByID() {
        autoCAFnumber = (AutoCompleteTextView) findViewById(R.id.autoCAFnumber);
        lvByDate = (ListView) findViewById(R.id.lvByDateResult);
        //etCAF = (EditText) findViewById(R.id.etCAFnumber);
        etDEL = (EditText) findViewById(R.id.etDELnumber);
        tvFromDate = (TextView) findViewById(R.id.etfromDate);
        tvToDate = (TextView) findViewById(R.id.etToDate);
        btnSearch =(Button)findViewById(R.id.btnSearch);
        dateFrom = (ImageView) findViewById(R.id.btnDateFrom);
        dateTo = (ImageView) findViewById(R.id.btnDateTo);
        linearLayout = (LinearLayout) findViewById(R.id.lLListLayout);
        linearLayout.setVisibility(View.GONE);

        charAutoSearch = autoCAFnumber.getText();
        strDEL = etDEL.getText().toString();
        strFromDate = tvFromDate.getText().toString();
        strToDate = tvToDate.getText().toString();
        // Autocomplete Text view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, strCAFNo);
        autoCAFnumber.setThreshold(2);
        autoCAFnumber.setAdapter(adapter);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch();
            }
        });
        setCurrentDate();
        fromDate();
        toDate();
    }

    public void fromDate() {
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass();
                dialogfragment.show(getFragmentManager(), "Date Picker Dialog");
            }
        });
    }

    public void toDate() {
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogfragment = new DatePickerDialogClass1();
                dialogfragment.show(getFragmentManager(), "Date Picker Dialog");
            }
        });
    }

    public void setCurrentDate() {
        Calendar c1 = Calendar.getInstance();
        System.out.println("Current time => " + c1.getTime());
        //String strTime=c1.getTime().toString();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //SimpleDateFormat tf = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c1.getTime());
        tvToDate.setText(formattedDate);
        strToDate = tvToDate.getText().toString();
    }

    public static class DatePickerDialogClass extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(), this, year, month, day);
            Window window = datepickerdialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.BOTTOM;
        /*    wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);*/
            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView tvFromDate = (TextView) getActivity().findViewById(R.id.etfromDate);
            tvFromDate.setText(year + "-" + (month + 1) + "-" + day);
            //tvFromDate.setText(day + "-" + (month + 1) + "-" + year);
        }
    }

    public static class DatePickerDialogClass1 extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datepickerdialog1 = new DatePickerDialog(getActivity(), this, year, month, day);

            //to change gravity we have to use window function
            Window window = datepickerdialog1.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();

            wlp.gravity = Gravity.BOTTOM;
            return datepickerdialog1;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView tvToDate = (TextView) getActivity().findViewById(R.id.etToDate);
            //tvToDate.setText(day + "-" + (month + 1) + "-" + year);
            tvToDate.setText(year + "-" + (month + 1) + "-" + day);
        }
    }

    /*This is custom alert dialog
    * to show pop up of customer details on btn click*/

    public void btnSearch() {
        try {
            if (autoCAFnumber.getText().toString().equals("") && etDEL.getText().toString().equals("")) {
                Toast.makeText(Search_activity.this, "Searching Data by Date", Toast.LENGTH_LONG).show();
                int success1;
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("frmdate", tvFromDate.getText().toString()));
                params2.add(new BasicNameValuePair("todate", tvToDate.getText().toString()));
                JSONObject json2 = jsonParser.makeHttpRequest(Config.TrackDetail, "POST", params2);
                Log.d("DATEWISE_data", json2.toString());
                if (json2.toString().equals("")) {
                    Toast.makeText(Search_activity.this, "Sorry no record found", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
                        HashMap<String, String> map;
                        success1 = json2.getInt(TAG_SUCCESS1);
                        if (success1 == 1) {
                            products1 = json2.getJSONArray(TAG_PRODUCTS1);
                            String vender_name[] = new String[products1.length()];
                            for (int i1 = 0; i1 < products1.length(); i1++) {
                                JSONObject c = products1.getJSONObject(i1);
                                cafNumber = c.getString("caf_no");
                                cust_name = c.getString("cust_name");
                                sub_date = c.getString("sub_date");
                                fnl_status = c.getString("fnl_status");
                                fnl_rating = c.getString("rating");
                                fnl_remark = c.getString("av_fnl_dtl_remark");
                                arrayList.add(new Tracking_Details_model(cafNumber, cust_name, sub_date, fnl_status, fnl_rating, fnl_remark));
                                //    SharedPreferences = getSharedPreferences(NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("caf_no1", cafNumber);
                                editor.apply();
                                //Toast.makeText(Search_activity.this, "welcome" + " " + cafNumber, Toast.LENGTH_LONG).show();
                                // insertDialogData();
                                addListDetails();
                                clearFields();
                            }
                            customListAdapter.notifyDataSetChanged();
                        }
                        if (success1 == 0) {
                            Toast.makeText(Search_activity.this, "Record not found", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            } else {
                int success1;
                List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                params2.add(new BasicNameValuePair("caf_no", autoCAFnumber.getText().toString()));
                params2.add(new BasicNameValuePair("dell_no", etDEL.getText().toString()));
                JSONObject json2 = jsonParser.makeHttpRequest("http://192.168.1.111/addVeTata/android/docomo/details1.php", "POST", params2);
                Log.d("data", json2.toString());
                if (json2.toString().equals("")) {
                    Toast.makeText(Search_activity.this, "Sorry no record found", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        final ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
                        HashMap<String, String> map;
                        success1 = json2.getInt(TAG_SUCCESS1);
                        if (success1 == 1) {
                            products1 = json2.getJSONArray(TAG_PRODUCTS1);
                            String vender_name[] = new String[products1.length()];
                            for (int i1 = 0; i1 < products1.length(); i1++) {
                                JSONObject c = products1.getJSONObject(i1);
                                // String id = c.getString(TAG_PID1);
                                cafNumber = c.getString("caf_no");
                                cust_name = c.getString("cust_name");
                                sub_date = c.getString("sub_date");
                                fnl_status = c.getString("fnl_status");
                                fnl_rating = c.getString("rating");
                                fnl_remark = c.getString("av_fnl_dtl_remark");
                                //    SharedPreferences = getSharedPreferences(NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("caf_no1", cafNumber);
                                editor.apply();
                                Toast.makeText(Search_activity.this, "welcome" + cafNumber, Toast.LENGTH_LONG).show();
                                insertDialogData(cafNumber, cust_name, sub_date, fnl_status, fnl_rating, fnl_remark);
                                clearFields();
                            }
                        }
                        if (success1 == 0) {
                            Toast.makeText(Search_activity.this, "Record not found", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class AsynchTrackingDetails extends AsyncTask<Void, Void, Void> {
        String result;
        String URL = Config.TrackDetail;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nDialog = new ProgressDialog(Search_activity.this);
            nDialog.setTitle("Checking Network");
            nDialog.setMessage("Loading..");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            /*{
                if (autoCAFnumber.getText().equals("")) {
                    Toast.makeText(Search_activity.this, "Please Enter Username Or Password", Toast.LENGTH_LONG).show();
                } else */
            {
                   /* int success1;
                    List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                    params2.add(new BasicNameValuePair("caf_no", (String) charAutoSearch));
                    //params2.add(new BasicNameValuePair("password", metPass.getText().toString()));
                    JSONObject json2 = jsonParser.makeHttpRequest("http://192.168.1.111/addVeTata/android/docomo/details1.php", "POST", params2);
                    Log.d("data", json2.toString());
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
                                cafNumber = c.getString("caf_no");
                                cust_name = c.getString("cust_name");
                                sub_date= c.getString("sub_date");
                                fnl_status = c.getString("fnl_status");
                                fnl_rating = c.getString("rating");
                                fnl_remark = c.getString("av_fnl_dtl_remark");
                                //    SharedPreferences = getSharedPreferences(NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("caf_no1", cafNumber);
                                editor.apply();
                                Toast.makeText(Search_activity.this, "welcome" + " " + cafNumber, Toast.LENGTH_LONG).show();

                               *//* try {
                                    AlertDialog.Builder builder;
                                    Context mContext = login.this;
                                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
                                    View layout = inflater.inflate(R.layout.custom_dialog_box,
                                            (ViewGroup) findViewById(R.id.layout_root));
                                   final TextView textCafNo = (TextView) layout.findViewById(R.id.tvCafNumber);
                                   final TextView textDelNo = (TextView) layout.findViewById(R.id.tvDelNumber);
                                  *//**//*  textCustName = (TextView) findViewById(R.id.tvCustName);
                                    textDate = (TextView) findViewById(R.id.tvDate);
                                    textStatus = (TextView) findViewById(R.id.tvStatus);
                                    textRating = (TextView) findViewById(R.id.tvRating);
                                    textRemark = (TextView) findViewById(R.id.tvRemark);*//**//*
                                    ImageView close_dialog = (ImageView) layout.findViewById(R.id.imageView_custom_dialog_close);

                                    textCafNo.setText(name);

                                    builder = new AlertDialog.Builder(mContext);
                                    builder.setView(layout);
                                    dialog = builder.create();
                                    Window window = dialog.getWindow();
                                    WindowManager.LayoutParams wlp = window.getAttributes();

                                    wlp.gravity = Gravity.BOTTOM;
                                    Toast.makeText(login.this, "AlertDialog call", Toast.LENGTH_SHORT).show();
                                    dialog.show();
                                    close_dialog.setOnClickListener(new View.OnClickListener() {
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }*//*

                              *//*  Intent i = new Intent(Search_activity.this, Search_activity.class);
                             *//**//*   i.putExtra("vendor_code", vendor_code);
                                i.putExtra("name", name);*//**//*
                                startActivity(i);*//*
                                *//*Intent i = new Intent(login.this, Search_activity.class);
                                startActivity(i);*//*
                            }
                        }
                        if (success1 == 0) {
                            Toast.makeText(Search_activity.this, "Wrong Username Or Password", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
               // }
            }catch (Exception e){
                e.printStackTrace();
            }*/
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            nDialog.dismiss();
        }
    }

    public void clearFields() {
        autoCAFnumber.setText("");
        etDEL.setText("");
        tvFromDate.setText("");
        tvToDate.setText("");
    }

    public void addListDetails() {
        try {
            linearLayout.setVisibility(View.VISIBLE);
            customListAdapter = new CustomListAdapter(this, arrayList);
            lvByDate.setAdapter(customListAdapter);

        } catch (IllegalStateException e1) {
            e1.printStackTrace();
        }
    }

    /*Custom adapter used to add custom list view */
    public class CustomListAdapter extends BaseAdapter {
        Context context_1;
        private LayoutInflater inflater = null;
        private ArrayList<Tracking_Details_model> pairs;

        public CustomListAdapter(Context context, ArrayList<Tracking_Details_model> pairs) {
            context_1 = context;
            this.pairs = pairs;
            inflater = (LayoutInflater) context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Tracking_Details_model getItem(int position) {
            return pairs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class Holder {
            TextView txt1, txt2, txt3;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.custom_listview_activity, null);
            try {

                holder.txt1 = (TextView) rowView.findViewById(R.id.text1);
                holder.txt2 = (TextView) rowView.findViewById(R.id.text2);
                holder.txt3 = (TextView) rowView.findViewById(R.id.text3);

                Tracking_Details_model tracking_details_model = pairs.get(position);
                holder.txt1.setText(tracking_details_model.getCaf_number());
                holder.txt2.setText(tracking_details_model.getStatus());
                holder.txt3.setText(tracking_details_model.getRating());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            /*holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
            holder.tv.setText(result[position]);
            holder.img.setImageResource(imageId[position]);*/

          /*  rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "You Clicked " + strCAFNo[position], Toast.LENGTH_LONG).show();
                }
            });*/
            return rowView;
        }
    }

    public void insertDialogData(String cAFNumber, String custName, String subDate, String status, String rating, String remark) {
        // public void insertDialogData() {
        database = new DatabaseClass(getApplicationContext());
        arrayList = new ArrayList<Tracking_Details_model>();
        arrayList = database.getAllTrackData();
        Tracking_Details_model details_model = new Tracking_Details_model();

        AlertDialog.Builder builder;
        Context mContext = Search_activity.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_dialog_box,
                (ViewGroup) findViewById(R.id.layout_root));
        ImageView close_dialog = (ImageView) layout.findViewById(R.id.imageView_custom_dialog_close);

        final TextView textCafNo = (TextView) layout.findViewById(R.id.tvCafNumber);
        //final TextView textDelNo = (TextView) layout.findViewById(R.id.tvDelNumber);
        final TextView textCustName = (TextView) layout.findViewById(R.id.tvCustName);
        final TextView textSubmiDate = (TextView) layout.findViewById(R.id.tvDate);
        final TextView textStatus = (TextView) layout.findViewById(R.id.tvStatus);
        final TextView textRating = (TextView) layout.findViewById(R.id.tvRating);
        final TextView textRemark = (TextView) layout.findViewById(R.id.tvRemark);

       /* textCafNo.setText(cafNumber);
        textDelNo.setText("");
        textCustName.setText(cust_name);
        textSubmiDate.setText(sub_date);
        textStatus.setText(fnl_status);
        textRating.setText(fnl_rating);
        textRemark.setText(fnl_remark);*/

        textCafNo.setText(cAFNumber);
        //textDelNo.setText("");
        textCustName.setText(custName);
        textSubmiDate.setText(subDate);
        textStatus.setText(status);
        textRating.setText(rating);
        textRemark.setText(remark);

        if (textStatus.getText().equals("Positive")) {
            textStatus.setTextColor(Color.GREEN);
        } else if (textStatus.getText().equals("Negative")) {
            textStatus.setTextColor(Color.RED);
        }
        builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;

        dialog.show();
        close_dialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(getApplicationContext(), AndroidDatabaseManager.class);
                startActivity(intent);

                etCAF.setText("");
                etDEL.setText("");
                break;
        }
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }
}
