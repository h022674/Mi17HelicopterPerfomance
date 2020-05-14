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

public class fragment_startcard extends Fragment implements fragment_starts_recyler.environmental_condition_listener {
    public interface send_env_cond{
        public void send_env_cond_to_takeoff(int altitude, int fat, int wind_kt, int wind_direction, boolean usemeter, boolean usemsec);
    }
    send_env_cond send_env_cond;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nulllayout, container, false);
        RecyclerView recyclerView=view.findViewById(R.id.recylerview);
        fragment_starts_recyler adapter;
        adapter=new fragment_starts_recyler(getContext(),this);
        //adapter.fragmentManager=getSupportFragmentManager();

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;


    }

    @Override
    public void environmental_condition_listener(int altitude, int fat, int wind_kt, int wind_direction, boolean usemeter, boolean usemsec) {

        send_env_cond.send_env_cond_to_takeoff(altitude,  fat,  wind_kt, wind_direction,usemeter,usemsec);

        }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            send_env_cond=(send_env_cond)getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }
}
