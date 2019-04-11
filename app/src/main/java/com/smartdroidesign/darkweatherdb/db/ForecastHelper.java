package com.smartdroidesign.darkweatherdb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ForecastHelper extends SQLiteOpenHelper {

    /**
     * Parameters for the constructor
     * @param context to use for locating paths to the the database
     * @param name of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older.
     */
    public static final String DB_NAME = "temperatures.db";
    public static final int DB_VERSION = 1;

    /**
     * PARAMETERS FOR THE onCreate() method.
     */
    public static final String TABLE_TEMPERATURES = "TEMPERATURES";
    private static final String COLUMN_ID = "_ID";
    public static final String COLUMN_TEMPERATURE = "TEMPERATURE";
    private static final String DB_CREATE =
            "CREATE TABLE " + TABLE_TEMPERATURES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TEMPERATURE + " REAL)";

    /*
    CONSTRUCTOR
     */
    public ForecastHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
