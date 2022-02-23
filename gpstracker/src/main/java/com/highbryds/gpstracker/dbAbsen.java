package com.highbryds.gpstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class dbAbsen {
    myDbHelper myhelper;
    public dbAbsen(Context context)
    {
        myhelper = new myDbHelper(context);
    }

    public long insertData(String lat, String lng, String acc, String time)
    {
        SQLiteDatabase dbb = myhelper.getWritableDatabase();
        long id = -1;
        try{
            dbb.beginTransaction();
            ContentValues contentValues = new ContentValues();
            contentValues.put(myDbHelper.LAT, lat);
            contentValues.put(myDbHelper.LNG, lng);
            contentValues.put(myDbHelper.ACC, acc);
            contentValues.put(myDbHelper.TIME, time);

            id = dbb.insert(myDbHelper.TABLE_NAME, null , contentValues);
            dbb.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            dbb.endTransaction();
            return id;
        }

    }

    public void truncateTabelTracking(){
        SQLiteDatabase db = myhelper.getWritableDatabase();
        try{
            db.beginTransaction();
            db.execSQL("delete from "+ myDbHelper.TABLE_NAME);
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }

    }

    public List<tempTracking> getDataListTracking()
    {
        SQLiteDatabase db = myhelper.getWritableDatabase();
        List<tempTracking> listTracking = new ArrayList<tempTracking>();

        try{
            db.beginTransaction();
            String[] columns = {myDbHelper.UID,myDbHelper.LAT,myDbHelper.LNG, myDbHelper.ACC, myDbHelper.TIME};
            Cursor cursor =db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
            while (cursor.moveToNext())
            {
                tempTracking data = new tempTracking();
                int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
                String lat =cursor.getString(cursor.getColumnIndex(myDbHelper.LAT));
                String lng =cursor.getString(cursor.getColumnIndex(myDbHelper.LNG));
                String acc =cursor.getString(cursor.getColumnIndex(myDbHelper.ACC));
                String time =cursor.getString(cursor.getColumnIndex(myDbHelper.TIME));
                data.setLat(lat);
                data.setLng(lng);
                data.setAcc(acc);
                data.setTime(time);

                listTracking.add(data);
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
            return listTracking;
        }
    }


    static class myDbHelper extends SQLiteOpenHelper
    {
        private static final String DATABASE_NAME = "myUMDb";    // Database Name
        private static final String TABLE_NAME = "tempTracking";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        private static final String LAT = "lat";    //Column II
        private static final String LNG = "lng";    //Column II
        private static final String ACC = "acc";    //Column II
        private static final String TIME = "time";    //Column II
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+LAT+" VARCHAR(255) ,"+ LNG+" VARCHAR(225) ,"+ ACC+" VARCHAR(225) ,"+ TIME+" VARCHAR(255));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;

        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {
            try {
                db.beginTransaction();
                db.execSQL(CREATE_TABLE);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                //Message.message(context,""+e);
            } finally {
                db.endTransaction();
            }
        }



        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
                //Message.message(context,"OnUpgrade");
                db.beginTransaction();
                db.execSQL(DROP_TABLE);
                db.setTransactionSuccessful();
            }catch (Exception e) {
                e.printStackTrace();
                //Message.message(context,""+e);
            }finally {
                db.endTransaction();
                onCreate(db);
            }
        }
    }
}