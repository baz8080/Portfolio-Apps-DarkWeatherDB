package com.smartdroidesign.darkweatherdb.services;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;

public class ForecastService {

    private static final String API_URL = "https://api.forecast.io/";

    /*
     * Define a service for getting forecast information using Retrofit by Square
     */
    public interface WeatherService {
        @GET("/forecast/{key}/{latitude},{longitude}")
        void getForecastAsync(
                @Path("key") String key,
                @Path("latitude") String lat,
                @Path("longitude") String longitude,
                Callback<Forecast> callback
        );
    }

    /*
     * Create an async call to the forecast service
     */
    public void loadForecastData(Callback<Forecast> callback) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

        WeatherService service = restAdapter.create(WeatherService.class);
        service.getForecastAsync("{your_api_key_here}", "53.349804", "-6.260310", callback);
    }

}
