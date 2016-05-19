package com.example.web3.myapplication;

import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends TabActivity {

    String strName, strMobile, strAddr;
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        tabHost = getTabHost();
        TabHost.TabSpec assignTab = tabHost.newTabSpec("tid1");
        TabHost.TabSpec pendingTab = tabHost.newTabSpec("tid1");
        TabHost.TabSpec completedTab = tabHost.newTabSpec("tid1");

/** TabSpec setIndicator() is used to set name for the tab. */
/** TabSpec setContent() is used to set content for a particular tab. */
        assignTab.setIndicator("First Tab Name").setContent(new Intent(this, OFR_Assign_activity.class));
        pendingTab.setIndicator("Second Tab Name").setContent(new Intent(this, OFR_Pending_activity.class));
        completedTab.setIndicator("Third Tab Name").setContent(new Intent(this, OFR_Completed_activity.class));

/** Add tabSpec to the TabHost to display. */
        tabHost.addTab(assignTab);
        tabHost.addTab(pendingTab);
        tabHost.addTab(completedTab);

       /* fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OFR_Customer_Details.class);
                startActivity(intent);
            }
        });*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addContactDetails();
            }
        });
    }

    public void addContactDetails() {
        // Creating alert Dialog with one Button
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth);

        Context context = getApplicationContext();
        alertDialog.setTitle("PASSWORD");
        alertDialog.setMessage("Enter Contact Details..");
        alertDialog.setIcon(R.drawable.ic_perm_identity_black_24dp);

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText etName = new EditText(context);
        etName.setHint("enter full name");
        layout.addView(etName);
        strName = etName.getText().toString();

        final EditText etMobile = new EditText(context);
        etMobile.setHint("enter mobile number");
        layout.addView(etMobile);
        strMobile = etMobile.getText().toString();

        final EditText etAddress = new EditText(getApplicationContext());
        etAddress.setHint("enter permanent address");
        layout.addView(etAddress);
        strAddr = etAddress.getText().toString();

        alertDialog.setView(layout);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Toast.makeText(getApplicationContext(), " ", Toast.LENGTH_SHORT).show();

                               /* Intent myIntent1 = new Intent(view.getContext(), Show.class);
                                startActivityForResult(myIntent1, 0);*/
                    }
                });
        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });
        // closed
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
