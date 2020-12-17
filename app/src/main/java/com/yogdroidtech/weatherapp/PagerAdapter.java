package com.yogdroidtech.weatherapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {


    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Fragment createdFragment;
        switch (position){
            case 0:
                createdFragment = new CurrentFragment();
                break;
            case 1:
                createdFragment = new ForecastFragment();
                break;
            case 2:
                createdFragment =new CityFragment();
                break;
            default:
                createdFragment =new SettingsFragment();
                break;

        }
        return createdFragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
