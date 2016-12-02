package com.example.bebe.hockey;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDBHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mjerenja.db";
    public static final String TABLE_NAME = "mjerenja_table";
    public static final String COLUMN_ID= "id";
    public static final String COLUMN_XGYRO= "x_gyro";
    public static final String COLUMN_YGYRO= "y_gyro";
    public static final String COLUMN_ZGYRO= "z_gyro";
    public static final String COLUMN_XACCEL = "x_accel";
    public static final String COLUMN_YACCEL = "y_accel";
    public static final String COLUMN_ZACCEL = "z_accel";
    public static final String COLUMN_TIMESTAMP = "time";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE" + TABLE_NAME + "(" +
                COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT " +
                COLUMN_XGYRO + "REAL " +
                COLUMN_YGYRO + "REAL" +
                COLUMN_ZGYRO + "REAL" +
                COLUMN_XACCEL + "REAL" +
                COLUMN_YACCEL + "REAL" +
                COLUMN_ZACCEL + "REAL" +
                COLUMN_TIMESTAMP + "TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);

    }

    public void addData (SetPodataka setPodataka)
    {
        ContentValues values = new ContentValues();
        values.put(COLUMN_XGYRO, setPodataka.getX_gyro());
        values.put(COLUMN_YGYRO, setPodataka.getY_gyro());
        values.put(COLUMN_ZGYRO, setPodataka.getZ_gyro());
        values.put(COLUMN_XACCEL, setPodataka.getX_accel());
        values.put(COLUMN_YACCEL, setPodataka.getY_accel());
        values.put(COLUMN_ZACCEL, setPodataka.getZ_accel());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

}
