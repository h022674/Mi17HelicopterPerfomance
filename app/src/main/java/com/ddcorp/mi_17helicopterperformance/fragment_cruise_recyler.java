package com.ddcorp.mi_17helicopterperformance;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

import static com.ddcorp.mi_17helicopterperformance.MainActivity.TAG;

class fragment_cruise_recyler extends RecyclerView.Adapter<fragment_cruise_recyler.ViewHolder> {
    public FragmentManager fragmentManager;
    public Integer dustprotect = 0, hoist = 0, hirss = 0, antiice = 0, internaltank = 0, bambibucket = 0, armour = 0;
    public int takeoff_alt;
    public int takeoff_fat;
    public boolean takeoff_usemeter;
    DatabaseAccess databaseAccess;
    Integer operating = 0, crew, fuel, load = 0, GW = 0;

    Integer singleeng25;
    Integer vmax;
    Integer vmin;
    Integer fuelconsumption;
    Integer singleeng30;

    Context context;

    Integer Cruise_fat;
    private Integer Cruise_alt;
    private Integer Cruise_alt_meter;

    private int card_0 = 0;
    private int card_1 = 1;
    private int card_2 = 2;
    card0_ViewHolder card0;
    public fuelconsumption_listener fuelconsumption_listener;
    private boolean noentry=true;

    public interface fuelconsumption_listener {
        public void fuelconsumption_listener(int fuelconsumption);

    }
    public fragment_cruise_recyler(Context context, fuelconsumption_listener fuelconsumption_listener) {
        this.context = context;
        // updateGrossWeight(GW, operating, crew, fuel, load, hoist, antiice, internaltank, bambibucket, armour);
        databaseAccess = DatabaseAccess.getInstance(context);
        this.fuelconsumption_listener = fuelconsumption_listener;


    }

    public Animation getBlinkAnimation() {
        Animation animation = new AlphaAnimation(1, 0);         // Change alpha from fully visible to invisible
        animation.setDuration(300);                             // duration - half a second
        animation.setInterpolator(new LinearInterpolator());    // do not alter animation rate
        animation.setRepeatCount(5);                            // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);             // Reverse animation at the end so the button will fade back in

        return animation;
    }

    @NonNull
    @Override
    public fragment_cruise_recyler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        if (viewType == card_0) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_cruise_card_0, parent, false);

            return new card0_ViewHolder(v);
        }

        if (viewType == card_1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_cruise_card_1, parent, false);

            return new card1_ViewHolder(v);
        }

        if (viewType == card_2) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_cruise_card_2, parent, false);

            return new card2_ViewHolder(v);
        }

        return null;
    }
    @Override
    public void onBindViewHolder(@NonNull fragment_cruise_recyler.ViewHolder holder, int position) {

        if (holder.getItemViewType() == card_0) {
            card0 = (card0_ViewHolder) holder;
            card0.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.clearFocus();
                }
            });
            if (card0.usemeter) {
                Cruise_alt_meter = Textview_to_integer(card0.Cruise_altitude_TextView.getText().toString());
                Cruise_alt = (int) (Math.round(Double.valueOf(Cruise_alt_meter) * 3.2808399));
            } else {
                Cruise_alt = Textview_to_integer(card0.Cruise_altitude_TextView.getText().toString());
                Cruise_alt_meter = (int) (Math.round(Double.valueOf(Cruise_alt) * 0.3048));
            }
            Cruise_fat = Textview_to_integer(card0.Cruise_fat_TextView.getText().toString());
            card0.Cruise_Checkbox_useMeter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (card0.Cruise_Checkbox_useMeter.isChecked()) {
                        card0.usemeter = true;
                        card0.textInputLayout_Cruise_alt.setHint("Altitude (mt.)");
                        if (!card0.Cruise_altitude_TextView.getText().toString().isEmpty()) {
                            Cruise_alt_meter = (int) (Math.round(Double.valueOf(Cruise_alt) * 0.3048));
                            card0.Cruise_altitude_TextView.setText(Cruise_alt_meter.toString());
                        }
                    } else {
                        card0.usemeter = false;
                        card0.textInputLayout_Cruise_alt.setHint("Altitude (Ft.)");
                        if (!card0.Cruise_altitude_TextView.getText().toString().isEmpty()) {
                            Cruise_alt = (int) (Math.round(Double.valueOf(Cruise_alt_meter) * 3.2808399));
                            card0.Cruise_altitude_TextView.setText(Cruise_alt.toString());
                        }
                    }


                }
            });
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!card0.Cruise_altitude_TextView.getText().toString().isEmpty() && !card0.Cruise_fat_TextView.getText().toString().isEmpty()) {
                        noentry=false;
                    }else noentry=true;

                    hidesoftkeyboard(v);
                    card0.checkBox_copyfromtakeoff.setChecked(false);
                    notifyDataSetChanged();


                }
            };
            card0.Cruise_altitude_TextView.setOnClickListener(onClickListener);
            card0.Cruise_fat_TextView.setOnClickListener(onClickListener);
            card0.checkBox_copyfromtakeoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked ) {
                        copyfromtakeoff();
                        noentry=false;
                        notifyDataSetChanged();
                    }

                }
            });
           /* if (card0.checkBox_copyfromtakeoff.isChecked()) {
                copyfromtakeoff();
                notifyDataSetChanged();

            }*/




        }
        if (holder.getItemViewType() == card_1) {
            final card1_ViewHolder card = (card1_ViewHolder) holder;
            if (!noentry) {
                card.NTKfactor = Double.valueOf(Cruise_alt_meter) / 1000 * 1.3;
                card.cruise1_1 = limitedoperation_NTK_calc(Cruise_fat, card.NTKfactor, 0.153, 90, 0.153, 90, 101.15, 0);
                card.cruise1_2 = card.cruise2_1 = limitedoperation_NTK_calc(Cruise_fat, card.NTKfactor, 0.153, 91, 0.137, 91, 101.15, 0);
                //card2_start.cruise2_1 = card2_start.cruise1_2;
                card.cruise2_2 = limitedoperation_NTK_calc(Cruise_fat, card.NTKfactor, 0.153, 91, 0.137, 92.9, 101.15, 0);
                //=(M59/1000)*1,3
                card.textView_cruise1_1.setText(new DecimalFormat("###.##").format(card.cruise1_1));
                card.textView_cruise1_2.setText(new DecimalFormat("###.##").format(card.cruise1_2));
                card.textView_cruise2_1.setText(new DecimalFormat("###.##").format(card.cruise2_1));
                card.textView_cruise2_2.setText(new DecimalFormat("###.##").format(card.cruise2_2));
                card.textView_NTKfactor.setText(new DecimalFormat("#.##").format(card.NTKfactor));
            }
        }
        if (holder.getItemViewType() == card_2) {
            final card2_ViewHolder card = (card2_ViewHolder) holder;
            if (!noentry) {
                try {
                    singleeng25 = Integer.valueOf(card.textView_singleeng25.getText().toString());
                    vmax = Integer.valueOf(card.textView_vmax.getText().toString());
                    vmin = Integer.valueOf(card.textView_vmin.getText().toString());
                    fuelconsumption = Integer.valueOf(card.textView_fuel.getText().toString());
                    singleeng30 = Integer.valueOf(card.textView_singleeng30.getText().toString());

                } catch (Exception e) {
                    Log.i(TAG, "update_airspeed_fuel_singleeng: exception ", e);
                }
                if (GW > 11100) {
                    vmax = databaseAccess.get_v_speeds(Cruise_alt_meter, "ustmax");
                    vmin = databaseAccess.get_v_speeds(Cruise_alt_meter, "ustmin");

                } else {
                    vmax = databaseAccess.get_v_speeds(Cruise_alt_meter, "altmax");
                    vmin = databaseAccess.get_v_speeds(Cruise_alt_meter, "altmin");
                }

                singleeng30 = databaseAccess.get_singleeng(Cruise_alt_meter, Cruise_fat, "singleeng30");
                singleeng25 = databaseAccess.get_singleeng(Cruise_alt_meter, Cruise_fat, "sineng25");
                fuelconsumption = databaseAccess.get_fuelconsumption(GW, Cruise_alt_meter);
                fuelconsumption_listener.fuelconsumption_listener(fuelconsumption);



                card.textView_vmax.setText(vmax.toString());
                card.textView_vmin.setText(vmin.toString());
                card.textView_singleeng25.setText(singleeng25.toString());
                card.textView_singleeng30.setText(singleeng30.toString());
                card.textView_fuel.setText(fuelconsumption.toString());
            }


        }


    }

    public void copyfromtakeoff() {
        if (card0 != null) {
            if (card0.checkBox_copyfromtakeoff.isChecked()) {
                Cruise_fat=takeoff_fat;
                card0.Cruise_fat_TextView.setText(String.valueOf(Cruise_fat));

                Cruise_alt=takeoff_alt+1000;
                Cruise_alt_meter=(int) (Math.round(Double.valueOf(Cruise_alt) * 0.3048));
                if (takeoff_usemeter) {
                    card0.Cruise_altitude_TextView.setText(Cruise_alt_meter.toString());
                }else card0.Cruise_altitude_TextView.setText(Cruise_alt.toString());

                card0.Cruise_Checkbox_useMeter.setChecked(takeoff_usemeter);

            }

        }

    }


    private int getIntegerformat(String time) {
        int min = -1;

        try {
            String[] split = time.split(":");
            if (split.length == 2) {
                if (Integer.parseInt(split[1]) < 60 && Integer.parseInt(split[0]) < 10000) {
                    min = Integer.parseInt(split[1]) + Integer.parseInt(split[0]) * 60;
                }
            }
        } finally {
            return min;
        }
    }

    private double limitedoperation_NTK_calc(Integer cruise_fat, double ntkfactor, double var1, double var2, double var3, double var4, double var5, double var6) {
        // ntk60_1 =EĞER(
        // EĞER(S5<0;((S5*0,153)+91+AD21);((S5*0,137)+92,9+AD21))<101,15;
        // EĞER(S5<0;((S5*0,153)+91+AD21);((S5*0,137)+92,9+AD21));101,15)

        //=EĞER(
        // (EĞER(R59<0;((R59*0,153)+90+Y69);((R59*0,153)+90+Y69)))>=101,15;101,15;
        // (EĞER(R59<0;((R59*0,153)+90+Y69);((R59*0,153)+90+Y69))))

        //=EĞER(
        // (EĞER(R59<0;((R59*0,153)+91+Y69);((R59*0,137)+91+Y69)))>=101,15;101,15;
        // (EĞER(R59<0;((R59*0,153)+91+Y69);((R59*0,137)+91+Y69))))

        //=EĞER(
        // (EĞER(R59<0;((R59*0,153)+91+Y69);((R59*0,137)+92,9+Y69)))>=101,15;101,15;
        // (EĞER(R59<0;((R59*0,153)+91+Y69);((R59*0,137)+92,9+Y69))))

        double returnvalue;
        if (cruise_fat < 0) {
            returnvalue = (cruise_fat * var1) + var2 + ntkfactor;
        } else {
            returnvalue = (cruise_fat * var3) + var4 + ntkfactor + var6;
        }
        if (returnvalue > var5) {
            returnvalue = var5;
        }

        return returnvalue;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void updateGrossWeight(Integer armour, Integer operating, Integer crew, Integer fuel, Integer load, Integer hoist, Integer antiice, Integer internaltank, Integer bambibucket, Integer GW, Integer dustprotect) {
        this.GW = GW;
        this.operating = operating;
        this.crew = crew;
        this.fuel = fuel;
        this.load = load;
        this.hoist = hoist;
        this.antiice = antiice;
        this.internaltank = internaltank;
        this.bambibucket = bambibucket;
        this.armour = armour;
        this.dustprotect = dustprotect;
    }

    private int Switch_to_integer(Boolean bool, int i) {
        if (bool) {
            return i;
        } else return 0;
    }

    private Integer Textview_to_integer(String Text) {
        Integer returnvalue = 0;
        try {
            if (Text != null) {
                returnvalue = Integer.parseInt(Text);
            }
        } catch (NumberFormatException e) {
            returnvalue = 0;
        }
        return returnvalue;
    }
    private void hidesoftkeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        view.clearFocus();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
    private class card0_ViewHolder extends ViewHolder {

        TextView Cruise_altitude_TextView,
                Cruise_fat_TextView;


        CheckBox Cruise_Checkbox_useMeter;
        CheckBox checkBox_copyfromtakeoff;
        TextInputLayout textInputLayout_Cruise_alt;

        private boolean usemeter;


        public card0_ViewHolder(View v) {
            super(v);
            Cruise_altitude_TextView = v.findViewById(R.id.Altitude_text);
            Cruise_fat_TextView = v.findViewById(R.id.FATtext);
            Cruise_Checkbox_useMeter = v.findViewById(R.id.checkBox_usemeter);
            checkBox_copyfromtakeoff = v.findViewById(R.id.checkBox_copyfromtakeoff);
            textInputLayout_Cruise_alt = v.findViewById(R.id.textInputLayout_Altitude_text);
            Cruise_alt = Textview_to_integer(Cruise_altitude_TextView.getText().toString());
            Cruise_fat = Textview_to_integer(Cruise_fat_TextView.getText().toString());
        }
    }
    private class card1_ViewHolder extends ViewHolder {

        TextView
                textView_cruise1_1,
                textView_cruise1_2,
                textView_cruise2_1,
                textView_cruise2_2,
                textView_NTKfactor;



        double cruise1_1;
        double cruise1_2;
        double cruise2_1;
        double cruise2_2;
        double NTKfactor;


        public card1_ViewHolder(View v) {
            super(v);

            textView_cruise1_1 = v.findViewById(R.id.textView_cruise1_1);
            textView_cruise1_2 = v.findViewById(R.id.textView_cruise1_2);
            textView_cruise2_1 = v.findViewById(R.id.textView_cruise2_1);
            textView_cruise2_2 = v.findViewById(R.id.textView_cruise2_2);
            textView_NTKfactor = v.findViewById(R.id.textView_NTKfactor);


        }
    }
    private class card2_ViewHolder extends ViewHolder {

        TextView
                textView_singleeng25,
                textView_vmax,
                textView_vmin,
                textView_fuel,
                textView_singleeng30;



        public card2_ViewHolder(View v) {
            super(v);


            textView_singleeng25 = v.findViewById(R.id.textView_singleeng25);
            textView_vmax = v.findViewById(R.id.textView_vmax);
            textView_vmin = v.findViewById(R.id.textView_vmin);
            textView_fuel = v.findViewById(R.id.textView_fuel);
            textView_singleeng30 = v.findViewById(R.id.textView_singleeng30);


        }
    }


}
