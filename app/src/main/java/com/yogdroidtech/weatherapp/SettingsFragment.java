package com.yogdroidtech.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    Button buttonCel, buttonFar;
    SharedPreferences sharedPreferences;
    Boolean isCelActive;
    TextView user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.settings_layout, container, false);

        buttonCel = (Button)view.findViewById(R.id.buttonCel);
        buttonFar = (Button)view.findViewById(R.id.buttonFar);
        user = (TextView)view.findViewById(R.id.user);

        user.setText(CurrentFragment.userName);
        buttonCel.setText("\u00B0"+"C");
        buttonFar.setText("\u00B0"+"F");

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        isCelActive = sharedPreferences.getBoolean("isCelActive", true);

        if(isCelActive){
            buttonFar.setBackgroundColor(getResources().getColor(R.color.inActiveButton));
            buttonCel.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        else {
            buttonFar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            buttonCel.setBackgroundColor(getResources().getColor(R.color.inActiveButton));
        }


        buttonCel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isCelActive){
                    Toast.makeText(getContext(), "Units already in Celsius", Toast.LENGTH_SHORT).show();
                }
                else {
                    buttonFar.setBackgroundColor(getResources().getColor(R.color.inActiveButton));
                    buttonCel.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    sharedPreferences.edit().putString("units","metric").commit();
                    sharedPreferences.edit().putBoolean("isCelActive",true).commit();
                }

            }
        });

        buttonFar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(isCelActive){
                  buttonFar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                  buttonCel.setBackgroundColor(getResources().getColor(R.color.inActiveButton));
                  sharedPreferences.edit().putString("units","imperial").commit();
                  sharedPreferences.edit().putBoolean("isCelActive",false).commit();
              }
              else {
                  Toast.makeText(getContext(), "Units already in Fahrenheit", Toast.LENGTH_SHORT).show();
              }
            }
        });
        return view;
    }
}
