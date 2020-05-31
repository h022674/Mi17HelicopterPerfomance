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

import com.google.firebase.crashlytics.FirebaseCrashlytics;

public class fragment_cruise extends Fragment implements fragment_cruise_recyler.fuelconsumption_listener {
    fragment_cruise_recyler adapter;
    public send_fuelconsumption send_fuelconsumption;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nulllayout, container, false);
        RecyclerView recyclerView=view.findViewById(R.id.recylerview);

        adapter=new fragment_cruise_recyler(getContext(),  this);
        //adapter.fragmentManager=getSupportFragmentManager();

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

       // send_fuelconsumption_to_arivalFragment.send_fuelconsumption_to_arivalFragment(adapter.fuelconsumption);
       /* FirebaseCrashlytics.getInstance().log("test");
        FirebaseCrashlytics.getInstance().setCustomKey("str_key", "hello");
*/
        //  Crashlytics.getInstance().crash();
        return view;


    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            send_fuelconsumption = (send_fuelconsumption) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }

    @Override
    public void fuelconsumption_listener(int fuelconsumption) {
        send_fuelconsumption.send_fuelconsumption_to_arivalFragment(fuelconsumption);
    }

    public interface send_fuelconsumption{
        public void send_fuelconsumption_to_arivalFragment(int fuelconsumption);
    }
     public void getEnvironmental_Conditions(int altitude, int fat, boolean usemeter) {
        if (adapter!= null) {
            adapter.takeoff_alt=altitude;
            adapter.takeoff_fat=fat;
            adapter.takeoff_usemeter =usemeter;
            adapter.copyfromtakeoff();
            /*
            adapter.card0.Takeoff_altitude_TextView.setText(String.valueOf(altitude));
            adapter.card0.Takeoff_fat_TextView.setText(String.valueOf(fat));
            adapter.card0.Takeoff_wind_TextView.setText(String.valueOf(wind_meter));
            adapter.card0.Takeoff_Spinner_Wind_Direction.setSelection(wind_direction);
            adapter.card0.Takeoff_Checkbox_useMeter.setChecked(takeoff_usemeter);
            adapter.card0.Takeoff_Checkbox_useMSec.setChecked(takeoff_usemsec);
*/

        }

    }


}
