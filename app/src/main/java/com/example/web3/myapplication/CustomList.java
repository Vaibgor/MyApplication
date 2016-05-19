package com.example.web3.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class CustomList extends Activity {
    String[] strCAFNo, strStatus, strRating;
    Search_activity context_1;
    ListView listView;
    Context context;
    ArrayList prgmName;
    public static String [] prgmImages={"1","2","3","4"};
    public static String [] prgmNameList={"Let Us C","c++","JAVA","Jsp"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);
        context=this;

        listView=(ListView) findViewById(R.id.lvByDateResult);
        listView.setAdapter(new CustomAdapter(this, prgmNameList,prgmImages));

    }

}
