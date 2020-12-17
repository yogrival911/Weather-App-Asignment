package com.yogdroidtech.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CityFragment extends Fragment {
    String appid = "8931dea8ea47fe58dee9f3e02a31c049";
    SharedPreferences sharedPreferences;
    String units;
    TextView dateView;
    CalendarView calendarView;
    Spinner spinnerCity;
    int increment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_layout, container, false);

        dateView = (TextView)view.findViewById(R.id.dateView);
        calendarView = (CalendarView)view.findViewById(R.id.calendarView);
        spinnerCity = (Spinner)view.findViewById(R.id.spinnerCity);

        units = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("units","metrics");

        increment = 0;

        long currentTime = System.currentTimeMillis();
        long maxDate = currentTime + 1000 * 60 * 60 * 24 * 7;
        long minDate = currentTime ;
        calendarView.setMinDate(minDate);
        calendarView.setMaxDate(maxDate);

        Date date = new Date((long) minDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        String dateString = dateFormat.format(date);
        int day = Integer.parseInt(dateString);

        Log.i("yogesh",day+"");
        Log.i("yogesh",increment+" increment");

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String searchCity = adapterView.getItemAtPosition(i).toString();
                Log.i("yogesh", searchCity);

                Retrofit retrofit = RetrofitClientInstance.getRetrofit();
                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                Call<CoordinateCity> coordinateCityCall = retrofitInterface.getCityToCo(searchCity,appid);
                coordinateCityCall.enqueue(new Callback<CoordinateCity>() {
                    @Override
                    public void onResponse(Call<CoordinateCity> call, Response<CoordinateCity> response) {
                        Log.i("yogesh", response.body().toString());

                        String lat = response.body().getCoord().getLat().toString();
                        String lon = response.body().getCoord().getLon().toString();

                        Retrofit retrofit = RetrofitClientInstance.getRetrofit();
                        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                        Call<WeatherData> weatherDataCall = retrofitInterface.getWeatherData(lat, lon, appid,units);
                        weatherDataCall.enqueue(new Callback<WeatherData>() {
                            @Override
                            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                                Log.i("yogesh", response.body().toString());
                                Log.i("yogesh", increment+"");
                            }

                            @Override
                            public void onFailure(Call<WeatherData> call, Throwable t) {

                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<CoordinateCity> call, Throwable t) {

                    }
                });



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                increment = i2-day;
                dateView.setText(increment+"");

                String searchCity = spinnerCity.getSelectedItem().toString();

                Retrofit retrofit = RetrofitClientInstance.getRetrofit();
                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                Call<CoordinateCity> coordinateCityCall = retrofitInterface.getCityToCo(searchCity,appid);
                coordinateCityCall.enqueue(new Callback<CoordinateCity>() {
                    @Override
                    public void onResponse(Call<CoordinateCity> call, Response<CoordinateCity> response) {
                        Log.i("yogesh", response.body().toString());

                        String lat = response.body().getCoord().getLat().toString();
                        String lon = response.body().getCoord().getLon().toString();

                        Retrofit retrofit = RetrofitClientInstance.getRetrofit();
                        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                        Call<WeatherData> weatherDataCall = retrofitInterface.getWeatherData(lat, lon, appid,units);
                        weatherDataCall.enqueue(new Callback<WeatherData>() {
                            @Override
                            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                                Log.i("yogesh", response.body().toString());
                                Log.i("yogesh", increment+"");
                            }

                            @Override
                            public void onFailure(Call<WeatherData> call, Throwable t) {

                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<CoordinateCity> call, Throwable t) {

                    }
                });


            }
        });
        return view;
    }
}
