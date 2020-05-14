package com.ddcorp.mi_17helicopterperformance;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class fragment_takeoff extends Fragment implements fragment_takeoff_recyler.environmental_condition_listener {
    fragment_takeoff_recyler adapter;
    int GW;
    send_env_cond send_env_cond;


    interface send_env_cond{
        void send_env_cond_to_cruise(int altitude, int fat, boolean usemeter);
        void send_env_cond_to_arrival(int altitude, int fat, boolean usemeter, int wind_kt, int wind_direction, boolean usemsec);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nulllayout, container, false);
        RecyclerView recyclerView=view.findViewById(R.id.recylerview);

        adapter=new fragment_takeoff_recyler(getContext(),this);
        //adapter.fragmentManager=getSupportFragmentManager();


        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;


    }

    public void getEnvironmental_Conditions(int altitude, int fat, int wind_meter, int wind_direction, boolean usemeter, boolean usemsec) {
        if (adapter.card0 != null) {
            adapter.card0.Takeoff_altitude_TextView.setText(String.valueOf(altitude));
            adapter.card0.Takeoff_fat_TextView.setText(String.valueOf(fat));
            adapter.card0.Takeoff_wind_TextView.setText(String.valueOf(wind_meter));
            adapter.card0.Takeoff_Spinner_Wind_Direction.setSelection(wind_direction);
            adapter.card0.Takeoff_Checkbox_useMeter.setChecked(usemeter);
            adapter.card0.Takeoff_Checkbox_useMSec.setChecked(usemsec);

        }

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            adapter.notifyDataSetChanged();
            // do something when visible.
        }
    }

    public void getGW(int gw) {

        if (adapter != null) {
           adapter.GW=gw;
        }

    }

    @Override
    public void environmental_condition_listener(int altitude, int fat, boolean usemeter, int wind_kt, int wind_direction, boolean usemsec) {
        send_env_cond.send_env_cond_to_cruise(altitude,  fat, usemeter);
        send_env_cond.send_env_cond_to_arrival(altitude,  fat, usemeter,wind_kt,wind_direction,usemsec);

    }





    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            send_env_cond=(send_env_cond) getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }
}
