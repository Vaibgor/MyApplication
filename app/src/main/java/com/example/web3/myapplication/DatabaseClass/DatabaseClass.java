package com.example.web3.myapplication.DatabaseClass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.web3.myapplication.model_class.Ofr_Assign_model;
import com.example.web3.myapplication.model_class.Tracking_Details_model;

import java.util.ArrayList;

/**
 * Created by scrm on 3/5/16.
 */
public class DatabaseClass extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trackDetails.db";
    private static final String TABLE_LOGIN_DETAILS = "loginDetails";
    private static final String TABLE_CUSTOMER_DETAILS = "customerDetails";
    private static final String TABLE_OFR_ASSIGN = "ofr_assignDaetails";
    private static final String TABLE_TRACK_DETAILS = "tacking_details";

    public DatabaseClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_LOGIN_DETAILS_TABLE = "CREATE TABLE " +
            TABLE_LOGIN_DETAILS + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, dealer_id TEXT )";

    private static final String CREATE_CUSTOMER_DETAILS_TABLE = "CREATE TABLE " +
            TABLE_CUSTOMER_DETAILS + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, MOBILE INTEGER, EMAIL TEXT, " +
            "ADDRESS TEXT )";

    private static final String CREATE_OFR_ASSIGN_TABLE = "CREATE TABLE " +
            TABLE_OFR_ASSIGN + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT )";

    private static final String CREATE_TRACKING_DETAILS_TABLE = "CREATE TABLE " +
            TABLE_TRACK_DETAILS + "( ID INTEGER PRIMARY KEY AUTOINCREMENT, CAF_NUMBER INTEGER," +
            "DEL_NUMBER INTEGER, CUSTOMER_NAME TEXT, DATE TEXT, STATUS TEXT, RATING TEXT, REMARK TEXT )";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LOGIN_DETAILS_TABLE);
        db.execSQL(CREATE_OFR_ASSIGN_TABLE);
        db.execSQL(CREATE_CUSTOMER_DETAILS_TABLE);
        db.execSQL(CREATE_TRACKING_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertLoginDetails(int dealerID) {
        long count = 0;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("dealer_id", dealerID);
            count=sqLiteDatabase.insert(CREATE_LOGIN_DETAILS_TABLE, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getLoginDetails() {
        int dealerID=0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT  * FROM " + CREATE_LOGIN_DETAILS_TABLE;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()){
                do {
                    dealerID=cursor.getInt(1);
                    db.close();
                }while (cursor.moveToNext());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return dealerID;
    }

    public long insertTrackingDetails(Tracking_Details_model trackDetails) {
        long count = 0;
        try {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("CAF_NUMBER", trackDetails.getCaf_number());
            values.put("DEL_NUMBER", trackDetails.getDel_number());
            values.put("CUSTOMER_NAME", trackDetails.getCust_name());
            values.put("DATE", trackDetails.getDate());
            values.put("STATUS", trackDetails.getStatus());
            values.put("RATING", trackDetails.getRating());
            values.put("REMARK", trackDetails.getRemark());
            count= sqLiteDatabase.insert(TABLE_TRACK_DETAILS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public ArrayList<Tracking_Details_model> getAllTrackData() {
        ArrayList<Tracking_Details_model> models = new ArrayList<Tracking_Details_model>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT  * FROM " + TABLE_TRACK_DETAILS;
            Cursor cursor = db.rawQuery(query, null);
            Tracking_Details_model trackDetails = null;

            if (cursor.moveToFirst()) {
                do {
                    trackDetails = new Tracking_Details_model();
                    trackDetails.setCaf_number(cursor.getString(1));
                    trackDetails.setDel_number(cursor.getString(2));
                    trackDetails.setCust_name(cursor.getString(3));
                    trackDetails.setDate(cursor.getString(4));
                    trackDetails.setStatus(cursor.getString(5));
                    trackDetails.setRating(cursor.getString(6));
                    trackDetails.setRemark(cursor.getString(7));
                    models.add(trackDetails);
                } while (cursor.moveToNext());
            }
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return models;
    }

// getdata for android databaseManagerClass

    public ArrayList<Cursor> getData(String Query) {
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[]{"mesage"};
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2 = new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try {
            String maxQuery = Query;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);
            //add value to cursor2
            Cursor2.addRow(new Object[]{"Success"});
            alc.set(1, Cursor2);
            if (null != c && c.getCount() > 0) {
                alc.set(0, c);
                c.moveToFirst();
                return alc;
            }
            return alc;
        } catch (SQLException sqlEx) {
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + sqlEx.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        } catch (Exception ex) {

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[]{"" + ex.getMessage()});
            alc.set(1, Cursor2);
            return alc;
        }


    }
}
