package com.smartdroidesign.darkweatherdb.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.smartdroidesign.darkweatherdb.R;
import com.smartdroidesign.darkweatherdb.db.ForecastDataSource;
import com.smartdroidesign.darkweatherdb.services.Forecast;
import com.smartdroidesign.darkweatherdb.services.ForecastService;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    // TODO: Declare mDataSource
    protected ForecastDataSource mDataSource;

    protected Button mInsertButton;
    protected Button mSelectButton;
    protected Button mUpdateButton;
    protected Button mDeleteButton;

    protected double[] mTemperatures;
    protected TextView mHighTextView;
    protected TextView mLowTextView;

    protected long mHighTemp;
    protected long mLowTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().hide();

        // TODO: Instantiate mDataSource
        /*
        create the DB source
        open a connection to it
         */
        mDataSource = new ForecastDataSource(MainActivity.this);
//         mDataSource.open(); -> moved to onResume()

        mHighTextView = findViewById(R.id.textView2);
        mLowTextView = findViewById(R.id.textView3);

        mInsertButton = findViewById(R.id.insertButton);
        mInsertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadForecastData();
            }
        });

        mSelectButton = findViewById(R.id.selectButton);
        mSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewForecastActivity.class));
            }
        });

        mUpdateButton = findViewById(R.id.updateButton);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Update
                mDataSource.updateTemperature(100);
            }
        });

        mDeleteButton = findViewById(R.id.deleteButton);
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Delete
                mDataSource.deleterAll();
            }
        });

        TextView photoCredit = findViewById(R.id.textView);
        photoCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.flickr.com/photos/london/71458818"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: Open db
        mDataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: Close db
        mDataSource.close();
    }

    protected void loadForecastData() {
        ForecastService service = new ForecastService();
        service.loadForecastData(mForecastCallback);
    }

    protected Callback<Forecast> mForecastCallback = new Callback<Forecast>() {
        @Override
        public void success(Forecast forecast, Response response) {
            mTemperatures = new double[forecast.hourly.data.size()];
            for (int i = 0; i < forecast.hourly.data.size(); i++) {
                mTemperatures[i] = forecast.hourly.data.get(i).temperature;
                Log.v(TAG, "Temp " + i + ": " + mTemperatures[i]);
            }

            // TODO: Insert
            mDataSource.insertForecast(forecast);
            updateHighAndLow();
            enableOtherButtons();
        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };

    private void updateHighAndLow() {
        // TODO: Query for high temp
        // TODO: Query for low temp

        mHighTextView.setText(String.valueOf("Upcoming high: " + mHighTemp).trim());
        mLowTextView.setText(String.valueOf("Upcoming low: " + mLowTemp).trim());

    }

    private void resetHighAndLow() {
        mHighTemp = 0;
        mLowTemp = 0;
        mHighTextView.setText("");
        mLowTextView.setText("");
    }

    private void enableOtherButtons() {
        mSelectButton.setEnabled(true);
        mUpdateButton.setEnabled(true);
        mDeleteButton.setEnabled(true);
    }
}