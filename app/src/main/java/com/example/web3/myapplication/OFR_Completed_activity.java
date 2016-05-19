package com.example.web3.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.web3.myapplication.DatabaseClass.DatabaseClass;
import com.example.web3.myapplication.model_class.Ofr_Assign_model;

import java.util.ArrayList;

public class OFR_Completed_activity extends AppCompatActivity {

    private static LayoutInflater inflater=null;

    ListView listView;
    DatabaseClass database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ofr__completed_activity);

    }
    public class CustomListAdapter extends BaseAdapter {

        private Context context_1;

        private ArrayList<Ofr_Assign_model> pairs;

        public CustomListAdapter(Context context, ArrayList<Ofr_Assign_model> pairs) {
            context_1 = context;
            this.pairs = pairs;
        }

        @Override
        public int getCount() {
            return pairs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        public class Holder {
            TextView txt1, txt2,txt3;
            ImageView img;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            Holder holder = new Holder();
            View rowView;
            rowView = inflater.inflate(R.layout.custom_listview_activity, null);
            holder.txt1 = (TextView) rowView.findViewById(R.id.text1);
            holder.txt2 = (TextView) rowView.findViewById(R.id.text2);
            holder.txt3 = (TextView) rowView.findViewById(R.id.text3);

            /*holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
            holder.tv.setText(result[position]);
            holder.img.setImageResource(imageId[position]);
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
                }
            });*/
            return rowView;
        }
    }
}
