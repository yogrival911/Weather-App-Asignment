package com.yogdroidtech.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    Button buttonCel, buttonFar;
    SharedPreferences sharedPreferences;
    Boolean isCelActive;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.settings_layout, container, false);

        buttonCel = (Button)view.findViewById(R.id.buttonCel);
        buttonFar = (Button)view.findViewById(R.id.buttonFar);

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
                buttonFar.setBackgroundColor(getResources().getColor(R.color.inActiveButton));
                buttonCel.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                sharedPreferences.edit().putString("units","metric").commit();
                sharedPreferences.edit().putBoolean("isCelActive",true).commit();

            }
        });

        buttonFar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonFar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                buttonCel.setBackgroundColor(getResources().getColor(R.color.inActiveButton));
                sharedPreferences.edit().putString("units","imperial").commit();
                sharedPreferences.edit().putBoolean("isCelActive",false).commit();
            }
        });
        return view;
    }
}
