package com.yogdroidtech.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForecastFragment extends Fragment {
    String appid = "8931dea8ea47fe58dee9f3e02a31c049";
    SharedPreferences sharedPreferences;
    String lat, lon, units;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.forecast_layout, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        lat = sharedPreferences.getString("savedLat", "30");
        lon = sharedPreferences.getString("savedLon", "74");
        units = sharedPreferences.getString("units","metric");

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        MyAdapter myAdapter = new MyAdapter(getContext(),CurrentFragment.weatherData);
        recyclerView.setAdapter(myAdapter);


        return view;
    }
}
