package com.ddcorp.mi_17helicopterperfomance;

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

public class fragment_weights extends Fragment implements fragment_weights_recyler.GrossWeight_listener {
    public sendGW sendGW;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nulllayout, container, false);
        RecyclerView recyclerView=view.findViewById(R.id.recylerview);
        fragment_weights_recyler adapter;
        adapter=new fragment_weights_recyler(getContext(),this);
        //adapter.fragmentManager=getSupportFragmentManager();

        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;


    }

    @Override
    public void GrossWeght_listener(int GW) {
        sendGW.sendGW_to_arrival_and_takeoff(GW);

    }

    public interface sendGW{
        public void sendGW_to_arrival_and_takeoff(int GW);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            sendGW = (sendGW) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }
}
