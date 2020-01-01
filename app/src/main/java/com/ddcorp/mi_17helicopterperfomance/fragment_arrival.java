package com.ddcorp.mi_17helicopterperfomance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class fragment_arrival extends Fragment {
    fragment_arrival_recyler adapter;
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nulllayout, container, false);
        recyclerView=view.findViewById(R.id.recylerview);

        adapter=new fragment_arrival_recyler(getContext());
        //adapter.fragmentManager=getSupportFragmentManager();

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;


    }

    public void getfuelconsumption(int fuelconsumption) {
        adapter.fuelconsumption=fuelconsumption;
      //  adapter.notifyDataSetChanged();
    }

    public void getGW(int gw) {
        adapter.GW=gw;
     //   adapter.notifyDataSetChanged();
       // recyclerView.setAdapter(adapter);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            adapter.notifyDataSetChanged();
            // do something when visible.
        }
    }

    public void getEnvironmental_Conditions(int altitude, int fat, int wind_kt, int wind_direction, boolean usemeter, boolean usemsec) {
        if (adapter.card != null) {

            adapter.takeoff_alt=altitude;
            adapter.takeoff_fat=fat;
            adapter.takeoff_usemeter =usemeter;
            adapter.takeoff_wind_kt=wind_kt;
            adapter.takeoff_wind_direction_position =wind_direction;
            adapter.takeoff_usemsec =usemsec;
            adapter.copyfromtakeoff();


           /* adapter.card.arrival_altitude_TextView.setText(String.valueOf(altitude));
            adapter.card.arrival_fat_TextView.setText(String.valueOf(fat));
            adapter.card.arrival_wind_TextView.setText(String.valueOf(wind_kt));
            adapter.card.arrival_Spinner_Wind_Direction.setSelection(wind_direction);
            adapter.card.arrival_Checkbox_useMeter.setChecked(usemeter);
            adapter.card.arrival_Checkbox_useMSec.setChecked(takeoff_usemsec);
*/
        }
    }
}
