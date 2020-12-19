package com.yogdroidtech.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CurrentFragment extends Fragment {
    String lat, lon, units;
    ImageView imageViewIcon;
    TextView textViewTemp, textViewFeels,textViewDesc, textViewMin,textViewMax,textViewPress,textViewHumid,textViewVisible,textViewClouds;
    TextView  textViewWind, textViewUV;
    SharedPreferences sharedPreferences;
    public static Boolean isCelActive;
    public  static  WeatherData weatherData;
    public static  String userName;
    String jj;
    ProgressBar proBar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.current_layout, container, false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        lat = sharedPreferences.getString("savedLat", "30");
        lon = sharedPreferences.getString("savedLon", "74");
        units = sharedPreferences.getString("units","metric");
        userName = sharedPreferences.getString("userName","");

        isCelActive = sharedPreferences.getBoolean("isCelActive", true);

        imageViewIcon = (ImageView)view.findViewById(R.id.imageViewIcon);
        textViewFeels = (TextView)view.findViewById(R.id.textViewFeels);
        textViewTemp = (TextView)view.findViewById(R.id.textViewTemp);
        textViewDesc = (TextView)view.findViewById(R.id.textViewDesc);
        textViewMin = (TextView)view.findViewById(R.id.textViewMin);
        textViewMax = (TextView)view.findViewById(R.id.textViewMax);
        textViewPress = (TextView)view.findViewById(R.id.textViewPress);
        textViewHumid = (TextView)view.findViewById(R.id.textViewHumid);
        textViewVisible = (TextView)view.findViewById(R.id.textViewVisible);
        textViewClouds =(TextView)view.findViewById(R.id.textViewClouds);
        textViewWind = (TextView)view.findViewById(R.id.textViewWind);
        textViewUV = (TextView)view.findViewById(R.id.textViewUV);
        proBar = (ProgressBar)view.findViewById(R.id.proBar);

        ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            networkCall();
        }
        else {
            Toast.makeText(getContext(), "Turn On Internet",Toast.LENGTH_LONG).show();
        }


        return view;
    }

    public void networkCall(){
        Retrofit retrofit = RetrofitClientInstance.getRetrofit();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        Call<WeatherData> dataCall = retrofitInterface.getWeatherData(lat,lon,units, RetrofitClientInstance.appID);
        dataCall.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                Log.i("yogesh", response.body().toString());

                weatherData = response.body();

                String iconCode = response.body().getCurrent().getWeather().get(0).getIcon();
                String url = "https://openweathermap.org/img/wn/"+iconCode+"@2x.png";
                Log.i("yogesh", url);
                Picasso.with(getActivity()).load(url).into(imageViewIcon);

                if(isCelActive){
                    textViewTemp.setText(response.body().getCurrent().getTemp().intValue()+"\u00B0"+"C");
                    textViewMin.setText(response.body().getDaily().get(0).getTemp().getMin().intValue()+"\u00B0"+"C");
                    textViewMax.setText(response.body().getDaily().get(0).getTemp().getMax().intValue()+"\u00B0"+"C");
                    textViewFeels.setText("Feels like "+response.body().getCurrent().getFeelsLike().intValue()+"\u00B0"+"C" );
                }
                else{
                    textViewTemp.setText(response.body().getCurrent().getTemp().intValue()+"\u00B0"+"F");
                    textViewMin.setText(response.body().getDaily().get(0).getTemp().getMin().intValue()+"\u00B0"+"F");
                    textViewMax.setText(response.body().getDaily().get(0).getTemp().getMax().intValue()+"\u00B0"+"F");
                    textViewFeels.setText("Feels like "+response.body().getCurrent().getFeelsLike().intValue()+"\u00B0"+"F" );
                }


                String description = response.body().getCurrent().getWeather().get(0).getDescription();
                textViewDesc.setText(description.substring(0,1).toUpperCase()+description.substring(1).toLowerCase());
                textViewPress.setText(response.body().getCurrent().getPressure()+" mBar");
                textViewHumid.setText(response.body().getCurrent().getHumidity()+" %");
                textViewClouds.setText(response.body().getCurrent().getClouds()+" %");
                textViewVisible.setText(response.body().getCurrent().getVisibility()+" meters");
                textViewWind.setText(response.body().getCurrent().getWindSpeed() + " m/s");
                textViewUV.setText(response.body().getCurrent().getUvi().toString());
                proBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Log.i("yogesh", t.toString());
                Toast.makeText(getContext(), "Data Parsing Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
