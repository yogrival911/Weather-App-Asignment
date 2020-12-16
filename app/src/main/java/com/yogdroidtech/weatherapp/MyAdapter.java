package com.yogdroidtech.weatherapp;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.MyViewHolder> {
    Context context;
    WeatherData weatherData;

    public MyAdapter(Context context, WeatherData weatherData) {
        this.context = context;
        this.weatherData = weatherData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
        return new MyAdapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        int timeStamp = weatherData.getDaily().get(position).getDt();
        Date date = new Date((long)timeStamp*1000);
        String dateBody = DateFormat.format("E, MMM dd", date.getTime()).toString();

        holder.date.setText(dateBody);
        holder.clear.setText(weatherData.getDaily().get(position).getWeather().get(0).getMain());
        holder.cloud.setText(weatherData.getDaily().get(position).getClouds()+"%");
        String iconCode = weatherData.getDaily().get(position).getWeather().get(0).getIcon();
        String iconUrl = "https://openweathermap.org/img/wn/"+iconCode+"@2x.png";
        Picasso.with(context).load(iconUrl).into(holder.icon);
        holder.minMax.setText(weatherData.getDaily().get(position).getTemp().getMin().intValue() + "/" + weatherData.getDaily().get(position).getTemp().getMin().intValue()+" \u2103");

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView date,clear,cloud,minMax;
        ImageView icon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = (TextView)itemView.findViewById(R.id.date);
            icon = (ImageView)itemView.findViewById(R.id.iconWeather);
            clear = (TextView)itemView.findViewById(R.id.clear);
            cloud = (TextView)itemView.findViewById(R.id.cloud);
            minMax = (TextView)itemView.findViewById(R.id.minMax);
        }
    }
}
