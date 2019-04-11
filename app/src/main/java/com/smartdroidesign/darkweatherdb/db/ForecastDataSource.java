package com.smartdroidesign.darkweatherdb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.smartdroidesign.darkweatherdb.services.Forecast;

public class ForecastDataSource {

    private SQLiteDatabase mDatabase;
    private ForecastHelper mForecastHelper;
    private Context mContext;

    public ForecastDataSource(Context context) {
        mContext = context;
        mForecastHelper = new ForecastHelper(context);
    }

    /*
    DB CRUD
     */

    // open

    /**
     * Will create a DB if it doesn't exist, opens it up, if it does
     *
     * @return a read/write database object valid until {@link #//close} is called
     * @throws SQLiteException if the database cannot be opened for writing
     */
    public void open() throws SQLException {
        mDatabase = mForecastHelper.getWritableDatabase();
    }

    // close
    public void close() {
        mDatabase.close();
    }

    /**
     *
     * @param forecast forecast condition parameter
     */
    public void insertForecast(Forecast forecast) {
        mDatabase.beginTransaction();

        try {
            for (Forecast.HourData hour : forecast.hourly.data) {
                ContentValues values = new ContentValues();
                values.put(ForecastHelper.COLUMN_TEMPERATURE, hour.temperature);
                mDatabase.insert(ForecastHelper.TABLE_TEMPERATURES, null, values);
            }
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }

    // select
    public Cursor selectAllTemperatures() {
        return mDatabase.query(
                ForecastHelper.TABLE_TEMPERATURES, // table
                new String[]{ ForecastHelper.COLUMN_TEMPERATURE }, // column names
                null, // where clause
                null, // where params
                null, // group by
                null, // having
                null // order by

        );
    }
    public Cursor selectTempsGreaterThan(String mintemp) {
        String whereClause = ForecastHelper.COLUMN_TEMPERATURE + " > ?";
        return mDatabase.query(
                ForecastHelper.TABLE_TEMPERATURES, // table
                new String[]{ ForecastHelper.COLUMN_TEMPERATURE }, // column names
                whereClause, // where clause
                new String[] { mintemp }, // where params
                null, // group by
                null, // having
                null // order by

        );
    }


    // update
    public void updateTemperature (double newTemp) {
        ContentValues values = new ContentValues();
        values.put(ForecastHelper.COLUMN_TEMPERATURE, newTemp);
        mDatabase.update(
                ForecastHelper.TABLE_TEMPERATURES, // table
                values, // values
                null, // where clause
                null // where params
        );
    }

    // delete
    public void deleterAll() {
        mDatabase.delete(
                ForecastHelper.TABLE_TEMPERATURES, // table
                null, // where clause
                null // where params
        );
    }
}
