package com.smartdroidesign.darkweatherdb.ui;

import android.app.ActionBar;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.smartdroidesign.darkweatherdb.R;
import com.smartdroidesign.darkweatherdb.db.ForecastDataSource;
import com.smartdroidesign.darkweatherdb.db.ForecastHelper;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ViewForecastActivity extends ListActivity {

    // TODO: Declare mDataSource

    private ArrayList<BigDecimal> mTemperatures;
    private ForecastDataSource mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_forecast);

        configureActionBar();

        // TODO: Instantiate mDataSource
        mDataSource = new ForecastDataSource(ViewForecastActivity.this);
        mTemperatures = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: Open db
        mDataSource.open();

        Cursor cursor = mDataSource.selectAllTemperatures();

        // TODO: Select all
        updateList(cursor);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: Close db
        mDataSource.close();
    }

    protected void updateList(Cursor cursor) {
        mTemperatures.clear();

        // TODO: Loop through cursor to populate mTemperatures

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // do stuff
            int i = cursor.getColumnIndex(ForecastHelper.COLUMN_TEMPERATURE);
            double temperature = cursor.getDouble(i);
            mTemperatures.add(new BigDecimal(temperature));
            cursor.moveToNext();
        }
        ArrayAdapter<BigDecimal> adapter = new ArrayAdapter<>(ViewForecastActivity.this,
                android.R.layout.simple_list_item_1,
                mTemperatures);

        setListAdapter(adapter);
    }


    protected void filterTemperatures(String minTemp) {
        // TODO: Select greater than
        Cursor cursor = mDataSource.selectTempsGreaterThan(minTemp);
        updateList(cursor);
    }

    protected void configureActionBar() {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.ab_filter);
        }

        assert actionBar != null;
        final EditText minTempField = actionBar.getCustomView().findViewById(R.id.minTempField);
        minTempField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                filterTemperatures(minTempField.getText().toString());
                return false;
            }
        });

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
    }
}
