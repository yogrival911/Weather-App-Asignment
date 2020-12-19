package com.yogdroidtech.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
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
    WeatherData downWeatherData;
    TextView cityTemp, cityDesc, cityFeels, cityMorn,cityDay,cityNight;
    ImageView cityIcon;
    Boolean isCelActive;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.city_layout, container, false);

        dateView = (TextView)view.findViewById(R.id.dateView);
        calendarView = (CalendarView)view.findViewById(R.id.calendarView);
        spinnerCity = (Spinner)view.findViewById(R.id.spinnerCity);
        cityTemp = (TextView)view.findViewById(R.id.cityTemp);
        cityDesc = (TextView)view.findViewById(R.id.cityDesc);
        cityFeels = (TextView)view.findViewById(R.id.cityFeels);
        cityMorn = (TextView)view.findViewById(R.id.cityMorn);
        cityDay = (TextView)view.findViewById(R.id.cityDay);
        cityNight = (TextView)view.findViewById(R.id.cityNight);

        cityIcon = (ImageView)view.findViewById(R.id.cityIcon);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        units = sharedPreferences.getString("units","metric");
        isCelActive = sharedPreferences.getBoolean("isCelActive", true);

        increment = 0;
        downWeatherData = new WeatherData();
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

        String searchCityOnCreate = spinnerCity.getSelectedItem().toString();
        Retrofit retrofit = RetrofitClientInstance.getRetrofit();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<CoordinateCity> coordinateCityCall = retrofitInterface.getCityToCo(searchCityOnCreate,RetrofitClientInstance.appID);
        coordinateCityCall.enqueue(new Callback<CoordinateCity>() {
            @Override
            public void onResponse(Call<CoordinateCity> call, Response<CoordinateCity> response) {
                String lat = response.body().getCoord().getLat().toString();
                String lon = response.body().getCoord().getLon().toString();

                Retrofit retrofit = RetrofitClientInstance.getRetrofit();
                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                Call<WeatherData> weatherDataCall = retrofitInterface.getWeatherData(lat, lon, units,RetrofitClientInstance.appID);
                weatherDataCall.enqueue(new Callback<WeatherData>() {
                    @Override
                    public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                        Log.i("yogesh", response.body().toString());
                        Log.i("yogesh", increment+"");

                        downWeatherData = response.body();
                        setUI(downWeatherData, increment);
                    }

                    @Override
                    public void onFailure(Call<WeatherData> call, Throwable t) {
                        Toast.makeText(getContext(), "Data Parsing Failed", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onFailure(Call<CoordinateCity> call, Throwable t) {
                Toast.makeText(getContext(), "Data Parsing Failed", Toast.LENGTH_SHORT).show();
            }
        });

                spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String searchCity = adapterView.getItemAtPosition(i).toString();
                Log.i("yogesh", searchCity);

                Retrofit retrofit = RetrofitClientInstance.getRetrofit();
                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                Call<CoordinateCity> coordinateCityCall = retrofitInterface.getCityToCo(searchCity,RetrofitClientInstance.appID);
                coordinateCityCall.enqueue(new Callback<CoordinateCity>() {
                    @Override
                    public void onResponse(Call<CoordinateCity> call, Response<CoordinateCity> response) {
                        Log.i("yogesh", response.body().toString());

                        String lat = response.body().getCoord().getLat().toString();
                        String lon = response.body().getCoord().getLon().toString();

                        Retrofit retrofit = RetrofitClientInstance.getRetrofit();
                        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                        Call<WeatherData> weatherDataCall = retrofitInterface.getWeatherData(lat, lon, units,RetrofitClientInstance.appID);
                        weatherDataCall.enqueue(new Callback<WeatherData>() {
                            @Override
                            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                                Log.i("yogesh", response.body().toString());
                                Log.i("yogesh", increment+"");

                                downWeatherData = response.body();
                                setUI(downWeatherData,increment);
                            }

                            @Override
                            public void onFailure(Call<WeatherData> call, Throwable t) {
                                Toast.makeText(getContext(), "Data Parsing Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<CoordinateCity> call, Throwable t) {
                        Toast.makeText(getContext(), "Data Parsing Failed", Toast.LENGTH_SHORT).show();
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
                dateView.setText(i2+"-"+(i1+1)+"-"+i);

                String searchCity = spinnerCity.getSelectedItem().toString();

                Retrofit retrofit = RetrofitClientInstance.getRetrofit();
                RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                Call<CoordinateCity> coordinateCityCall = retrofitInterface.getCityToCo(searchCity,RetrofitClientInstance.appID);
                coordinateCityCall.enqueue(new Callback<CoordinateCity>() {
                    @Override
                    public void onResponse(Call<CoordinateCity> call, Response<CoordinateCity> response) {
                        Log.i("yogesh", response.body().toString());

                        String lat = response.body().getCoord().getLat().toString();
                        String lon = response.body().getCoord().getLon().toString();

                        Retrofit retrofit = RetrofitClientInstance.getRetrofit();
                        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

                        Call<WeatherData> weatherDataCall = retrofitInterface.getWeatherData(lat, lon, units,appid);
                        weatherDataCall.enqueue(new Callback<WeatherData>() {
                            @Override
                            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                                Log.i("yogesh", response.body().toString());
                                Log.i("yogesh", increment+"");
                                downWeatherData = response.body();
                                setUI(downWeatherData,increment);
                            }

                            @Override
                            public void onFailure(Call<WeatherData> call, Throwable t) {
                                Toast.makeText(getContext(), "Data Parsing Failed", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onFailure(Call<CoordinateCity> call, Throwable t) {
                        Toast.makeText(getContext(), "Data Parsing Failed", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        return view;
    }

    public void setUI(WeatherData downWeatherData, int increment){
        Log.i("yogesh","download function "+ downWeatherData.toString());
        Log.i("yogesh","increment  "+ increment+"");

        cityDesc.setText(downWeatherData.getDaily().get(increment).getWeather().get(0).getMain());

        String iconCode = downWeatherData.getDaily().get(increment).getWeather().get(0).getIcon();
        String url = "https://openweathermap.org/img/wn/"+iconCode+"@2x.png";
        Log.i("yogesh", url);
        Picasso.with(getActivity()).load(url).into(cityIcon);

        if(isCelActive){
            cityTemp.setText(downWeatherData.getDaily().get(increment).getTemp().getMin().intValue()
                    +"/"+ downWeatherData.getDaily().get(increment).getTemp().getMax().intValue()+"\u00B0"+"C");
            cityMorn.setText(downWeatherData.getDaily().get(increment).getTemp().getMorn().intValue()+"\u00B0"+"C");
            cityDay.setText(downWeatherData.getDaily().get(increment).getTemp().getDay().intValue()+"\u00B0"+"C");
            cityNight.setText(downWeatherData.getDaily().get(increment).getTemp().getNight().intValue()+"\u00B0"+"C");
        }
        else {
            cityTemp.setText(downWeatherData.getDaily().get(increment).getTemp().getMin().intValue()
                    +"/"+ downWeatherData.getDaily().get(increment).getTemp().getMax().intValue()+"\u00B0"+"F");
            cityMorn.setText(downWeatherData.getDaily().get(increment).getTemp().getMorn().intValue()+"\u00B0"+"F");
            cityDay.setText(downWeatherData.getDaily().get(increment).getTemp().getDay().intValue()+"\u00B0"+"F");
            cityNight.setText(downWeatherData.getDaily().get(increment).getTemp().getNight().intValue()+"\u00B0"+"F");
        }

    }
}
