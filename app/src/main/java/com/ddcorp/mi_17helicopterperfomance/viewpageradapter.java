package com.ddcorp.mi_17helicopterperfomance;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class viewpageradapter extends FragmentPagerAdapter {
    Context context;
    private String tabtitles[] = new String[]{"Start", "Take off", "Cruise", "Arrival"};

    public viewpageradapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return super.getItemPosition(object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            // Open FragmentTab1.java
            case 0:
                fragment_weights fragment_weights = new fragment_weights();
                return fragment_weights;

            // Open FragmentTab1.java
            case 1:
                fragment_startcard fragment_startcard = new fragment_startcard();
                return fragment_startcard;

            // Open FragmentTab2.java
            case 2:
                fragment_takeoff fragment_takeoff = new fragment_takeoff();
                return fragment_takeoff;

            // Open FragmentTab3.java
            case 3:
                fragment_cruise fragment_cruise = new fragment_cruise();
                return fragment_cruise;

            case 4:
                fragment_arrival fragment_arrival = new fragment_arrival();
                return fragment_arrival;

        }
        return null;


    }
}
