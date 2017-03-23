package com.example.bebe.hockey;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mjerenja.db";
    private static final String TABLE_NAME = "mjerenja";
    private static final String COLUMN_ID= "id";
    private static final String COLUMN_XGYRO= "x_gyro";
    private static final String COLUMN_YGYRO= "y_gyro";
    private static final String COLUMN_ZGYRO= "z_gyro";
    private static final String COLUMN_XACCEL = "x_accel";
    private static final String COLUMN_YACCEL = "y_accel";
    private static final String COLUMN_ZACCEL = "z_accel";
    private static final String COLUMN_FORCE = "force";
    private static final String COLUMN_SPIN = "spin";
    private static final String COLUMN_ACC = "acc";
    private static final String COLUMN_TIMESTAMP = "time";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER," +
                COLUMN_XGYRO + " REAL," +
                COLUMN_YGYRO + " REAL," +
                COLUMN_ZGYRO + " REAL," +
                COLUMN_XACCEL + " REAL," +
                COLUMN_YACCEL + " REAL," +
                COLUMN_ZACCEL + " REAL," +
                COLUMN_FORCE + " REAL," +
                COLUMN_SPIN + " REAL," +
                COLUMN_ACC + " REAL," +
                COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);

    }


    public boolean  addData (SetPodataka setPodataka)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, setPodataka.getId());
        values.put(COLUMN_XGYRO, setPodataka.getX_gyro());
        values.put(COLUMN_YGYRO, setPodataka.getY_gyro());
        values.put(COLUMN_ZGYRO, setPodataka.getZ_gyro());
        values.put(COLUMN_XACCEL, setPodataka.getX_accel());
        values.put(COLUMN_YACCEL, setPodataka.getY_accel());
        values.put(COLUMN_ZACCEL, setPodataka.getZ_accel());
        values.put(COLUMN_FORCE, setPodataka.getForce());
        values.put(COLUMN_SPIN, setPodataka.getSpin());
        values.put(COLUMN_ACC, setPodataka.getAcc());
        long result = db.insert(TABLE_NAME,null,values);
        db.close();
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public Cursor maxForce(SQLiteDatabase db) {

        String query = "SELECT MAX(force) FROM mjerenja";

        Cursor cursor = db.rawQuery(query,null);

        return cursor;

    }
    public Cursor maxAcc(SQLiteDatabase db) {

        String query = "SELECT MAX(acc) FROM mjerenja";

        Cursor cursor = db.rawQuery(query,null);

        return cursor;

    }
    public Cursor maxSpin(SQLiteDatabase db) {

        String query = "SELECT MAX(spin) FROM mjerenja";

        Cursor cursor = db.rawQuery(query,null);

        return cursor;

    }





}
