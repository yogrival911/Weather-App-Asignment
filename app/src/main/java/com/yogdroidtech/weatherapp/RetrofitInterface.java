package com.yogdroidtech.weatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("onecall?")
    Call<WeatherData> getWeatherData(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("units") String units,
            @Query("appid") String appid);

    @GET("weather")
    Call<CoordinateCity> getCityToCo(
            @Query("q") String cityName,
            @Query("appid") String appid);
}
