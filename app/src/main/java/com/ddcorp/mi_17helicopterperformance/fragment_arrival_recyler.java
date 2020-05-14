package com.ddcorp.mi_17helicopterperformance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

class fragment_arrival_recyler extends RecyclerView.Adapter<fragment_arrival_recyler.ViewHolder> {
    public FragmentManager fragmentManager;
    public Integer dustprotect = 0, hoist = 0, hirss = 0, antiice = 0, internaltank = 0, bambibucket = 0, armour = 0;
    public int fuelconsumption;
    public int takeoff_alt;
    public int takeoff_fat;
    public int GW;
    public int takeoff_wind_kt;
    public int takeoff_wind_direction_position;
    public boolean takeoff_usemsec;

    DatabaseAccess databaseAccess;
    Integer ftime=150;
    Context context;
    Integer arrival_alt = 0, arrival_fat = 0;
    Integer arrival_alt_meter = 0;
    String wind_direction;
    private int card_0 = 0;
    private int card_equiptment = 1;
    private int card_1 = 2;
    private int card_2 = 3;
    private Integer arrival_wind_msec;
    private Integer arrival_wind;
    private boolean noentry=true;
    card0_arrival_ViewHolder card;
    boolean takeoff_usemeter;
    private boolean usemsec;
    private boolean usemeter;


    public fragment_arrival_recyler(Context context) {
        this.context = context;
        databaseAccess = DatabaseAccess.getInstance(context);



    }

    @NonNull
    @Override
    public fragment_arrival_recyler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        if (viewType == card_0) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_arrival_card_0, parent, false);

            return new card0_arrival_ViewHolder(v);
        }
        if (viewType == card_equiptment) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_arrival_equipment_card, parent, false);

            return new card_equipment_ViewHolder(v);
        }

        if (viewType == card_1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_arrival_card_1, parent, false);

            return new card1_arrival_ViewHolder(v);
        }
        if (viewType == card_2) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_arrival_card_2, parent, false);

            return new card2_arrival_ViewHolder(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull fragment_arrival_recyler.ViewHolder holder, int position) {

        if (holder.getItemViewType() == card_0) {


            card = (card0_arrival_ViewHolder) holder;

            card.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.clearFocus();
                }
            });
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!card.arrival_altitude_TextView.getText().toString().isEmpty() && !card.arrival_fat_TextView.getText().toString().isEmpty()) {
                        noentry=false;
                    }else noentry=true;
                    hidesoftkeyboard(v);
                    card.checkBox_copyfromtakeoff.setChecked(false);
                    notifyDataSetChanged();

                }
            };
            card.arrival_altitude_TextView.setOnClickListener(onClickListener);
            card.arrival_fat_TextView.setOnClickListener(onClickListener);
            card.arrival_wind_TextView.setOnClickListener(onClickListener);

            if (!copyfromtakeoff()) {
                if (usemeter) {
                    arrival_alt_meter = Textview_to_integer(card.arrival_altitude_TextView.getText().toString());
                    arrival_alt = (int) (Math.round(Double.valueOf(arrival_alt_meter) * 3.2808399));
                } else {
                    arrival_alt = Textview_to_integer(card.arrival_altitude_TextView.getText().toString());
                    arrival_alt_meter = (int) (Math.round(Double.valueOf(arrival_alt) * 0.3048));
                }

                if (usemsec) {
                    arrival_wind_msec = Textview_to_integer(card.arrival_wind_TextView.getText().toString());
                    arrival_wind = (int) (Math.round(Double.valueOf(arrival_wind_msec) * 1.94384449412));
                } else {
                    arrival_wind = Textview_to_integer(card.arrival_wind_TextView.getText().toString());
                    arrival_wind_msec = (int) (Math.round(Double.valueOf(arrival_wind) * 0.514444444));
                }
            }
            card.arrival_Checkbox_useMeter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (card.arrival_Checkbox_useMeter.isChecked()) {
                        usemeter = true;
                        card.textInputLayout_arrival_alt.setHint("Altitude (mt.)");
                        if (!card.arrival_altitude_TextView.getText().toString().isEmpty()) {
                            arrival_alt_meter = (int) (Math.round(Double.valueOf(arrival_alt) * 0.3048));
                            card.arrival_altitude_TextView.setText(arrival_alt_meter.toString());
                        }
                    } else {
                        usemeter = false;
                        card.textInputLayout_arrival_alt.setHint("Altitude (Ft.)");
                        if (!card.arrival_altitude_TextView.getText().toString().isEmpty()) {
                            arrival_alt = (int) (Math.round(Double.valueOf(arrival_alt_meter) * 3.2808399));
                            card.arrival_altitude_TextView.setText(arrival_alt.toString());
                        }
                    }
                }
            });

            card.arrival_Checkbox_useMSec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (card.arrival_Checkbox_useMSec.isChecked()) {
                        arrival_wind_msec = (int) (Math.round(Double.valueOf(arrival_wind) * 0.514444444));
                        card.textInputLayout_arrival_wind.setHint("Wind (m/sec.)");
                        if (!card.arrival_wind_TextView.getText().toString().isEmpty() && arrival_wind > 0) {
                            card.arrival_wind_TextView.setText(arrival_wind_msec.toString());
                        }

                    } else {
                         arrival_wind = (int) (Math.round(Double.valueOf(arrival_wind_msec) * 1.94384449412));
                        card.textInputLayout_arrival_wind.setHint("Wind (Kt.)");
                        if (!card.arrival_wind_TextView.getText().toString().isEmpty() && arrival_wind > 0) {
                            card.arrival_wind_TextView.setText(arrival_wind.toString());
                        }
                    }

                }
            });

            card.arrival_Spinner_Wind_Direction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    wind_direction = card.arrival_Spinner_Wind_Direction.getItemAtPosition(position).toString();
                    notifyDataSetChanged();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
           card.arrival_Spinner_Wind_Direction.setOnTouchListener(new View.OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {
                   card.checkBox_copyfromtakeoff.setChecked(false);
                   return false;
               }
           });


            arrival_fat = Textview_to_integer(card.arrival_fat_TextView.getText().toString());

            card.checkBox_copyfromtakeoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked ) {
                        //copyfromtakeoff();
                        noentry=false;
                        notifyDataSetChanged();
                    }

                }
            });

        }
        if (holder.getItemViewType() == card_1) {
            final card1_arrival_ViewHolder card = (card1_arrival_ViewHolder) holder;
            if (!noentry) {

                card.OGEWind = databaseAccess.getIGE_OGE_WING_value(arrival_wind_msec, wind_direction, "windIge");
                card.IGEWind = databaseAccess.getIGE_OGE_WING_value(arrival_wind_msec, wind_direction, "windOge");
                card.textViewOGEWind.setText(card.OGEWind.toString());
                card.textViewIGEWind.setText(card.IGEWind.toString());

                card.IGEtable = databaseAccess.get_IGE_OGE_TABLE_value(arrival_alt_meter, arrival_fat, "ige");
                card.textViewIGEtable.setText(card.IGEtable.toString());
                card.OGETable = databaseAccess.get_IGE_OGE_TABLE_value(arrival_alt_meter, arrival_fat, "oge");
                card.textViewOGEtable.setText(card.OGETable.toString());



                card.IgeTotal = card.IGEtable + card.IGEWind- dustprotect - antiice - hirss;
                card.OgeTotal = card.OGETable + card.OGEWind- dustprotect - antiice - hirss;
                if (card.IgeTotal > 13000) {
                    card.IgeTotal = 13000;
                }
                if (card.OgeTotal > 13000) {
                    card.OgeTotal = 13000;
                }
                card.textViewIgeTotal.setText(String.valueOf(card.IgeTotal));
                card.textViewOGETotal.setText(String.valueOf(card.OgeTotal));

            }
        }
        if (holder.getItemViewType() == card_2) {


            final card2_arrival_ViewHolder card = (card2_arrival_ViewHolder) holder;

            if (!noentry) {


                card.NTKfactor = Double.valueOf(arrival_alt_meter) / 1000 * 1.3;
                //EĞER(EĞER(R74<0;((R74*0,153)+91+Y87);((R74*0,137)+92,9+Y87))<101,15;EĞER(R74<0;((R74*0,153)+91+Y87);((R74*0,137)+92,9+Y87));101,15)
                card.ntk60_1 = limitedoperation_NTK_calc(arrival_fat, card.NTKfactor, 0.153, 91, 0.137, 92.9, 101.15, 0);
                //=EĞER(EĞER(R74<0;((R74*0,167)+95,7+Y87);((R74*0,137)+95,7+Y87))<101,15;EĞER(R74<0;((R74*0,167)+95,7+Y87);((R74*0,137)+95,7+Y87));101,15)
                card.ntk60_2 = limitedoperation_NTK_calc(arrival_fat, card.NTKfactor, 0.167, 95.7, 0.137, 95.7, 101.15, 0);
                card.ntk30_1 = card.ntk60_2;
                //=EĞER((EĞER(R74<0;((R74*0,167)+95,7+Y87);((R74*0,137)+95,7+Y87))+1,02)<101,15;(EĞER(R74<0;((R74*0,167)+95,7+Y87);((R74*0,137)+95,7+Y87))+1,02);101,15)
                card.ntk30_2 = limitedoperation_NTK_calc(arrival_fat, card.NTKfactor, 0.167, 95.7, 0.137, 95.7, 101.15, 1.02);
                card.ntk25_1 = card.ntk30_2;


                card.textView_25_1.setText(new DecimalFormat("###.##").format(card.ntk25_1));
                card.textView_60_1.setText(new DecimalFormat("###.##").format(card.ntk60_1));
                card.textView_60_2.setText(new DecimalFormat("###.##").format(card.ntk60_2));
                card.textView_30_1.setText(new DecimalFormat("###.##").format(card.ntk30_1));
                card.textView_30_2.setText(new DecimalFormat("###.##").format(card.ntk30_2));
                card.textView_NTKfactor.setText(new DecimalFormat("###.##").format(card.NTKfactor));
            }


        }
        if (holder.getItemViewType() == card_equiptment) {

            final card_equipment_ViewHolder card = (card_equipment_ViewHolder) holder;
            CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    update_static_weights(card);
                    notifyDataSetChanged();
                }
            };

            card.Switch_dustprotect.setOnCheckedChangeListener(onCheckedChangeListener);
            card.Switch_hirss.setOnCheckedChangeListener(onCheckedChangeListener);
            card.Switch_antiice.setOnCheckedChangeListener(onCheckedChangeListener);

            card.editText_ftime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hidesoftkeyboard(v);
                    if (!card.editText_ftime.getText().toString().isEmpty()) {
                        if (card.editText_ftime.getText().toString().length() ==5) {
                            String s= card.editText_ftime.getText().toString().substring(2,3);
                            if (s.compareTo(":")!= 0) {
                                card.editText_ftime.setText("");
                                Toast.makeText(context,"invalid input!",Toast.LENGTH_SHORT).show();

                            }else {
                                ftime = getIntegerformat(card.editText_ftime.getText().toString());
                                if (ftime > 0) {
                                    updatefuelfield(card);
                                }
                            }
                        }else {
                            card.editText_ftime.setText("");
                            Toast.makeText(context,"invalid input!",Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            });
            updatefuelfield(card);
            update_static_weights(card);

        }
    }
    public boolean copyfromtakeoff() {
        if (card != null) {
            if (card.checkBox_copyfromtakeoff.isChecked()) {
                arrival_fat=takeoff_fat;
                card.arrival_fat_TextView.setText(String.valueOf(arrival_fat));

                arrival_alt=takeoff_alt;
                arrival_alt_meter=(int) (Math.round(Double.valueOf(takeoff_alt) * 0.3048));
                if (takeoff_usemeter) {
                    card.arrival_altitude_TextView.setText(arrival_alt_meter.toString());
                }else card.arrival_altitude_TextView.setText(arrival_alt.toString());
                usemeter=takeoff_usemeter;
                card.arrival_Checkbox_useMeter.setChecked(takeoff_usemeter);

                arrival_wind=takeoff_wind_kt;
                arrival_wind_msec=(int) (Math.round(Double.valueOf(takeoff_wind_kt) * 0.514444444));
                if (takeoff_usemsec) {
                   card.arrival_wind_TextView.setText(arrival_wind_msec.toString());
                }else card.arrival_wind_TextView.setText(arrival_wind.toString());

                usemsec= takeoff_usemsec;
                card.arrival_Checkbox_useMSec.setChecked(takeoff_usemsec);

                wind_direction=card.arrival_Spinner_Wind_Direction.getItemAtPosition(takeoff_wind_direction_position).toString();
                card.arrival_Spinner_Wind_Direction.setSelection(takeoff_wind_direction_position);
            return true;
            } else return false;

        }else return false;

    }


    private void updatefuelfield(card_equipment_ViewHolder card) {
        card.textView_ftimemin.setText(" (" + ftime.toString() + " min.)");
        if ( GW > 0) {
            card.textView_takeoff_GW.setText(String.valueOf(GW)+" kg.");
            double fuelconsump_lt;
            double totalfuelconsump;
            if (fuelconsumption > 0) {
                card.textView_consumption.setTextColor(context.getResources().getColor(R.color.text_Green));;
                fuelconsump_lt = fuelconsumption * 0.79;
                card.textView_fuel.setText("(~" + String.valueOf(Math.round(fuelconsump_lt)) + " kg/hr.)");
                totalfuelconsump = (fuelconsump_lt / 60 * ftime);
                card.textView_consumption.setText(String.valueOf(Math.round(totalfuelconsump)) + " kg.");

            } else {
                totalfuelconsump=0;
                card.textView_consumption.setText("no cruise data!");
                card.textView_consumption.setTextColor(context.getResources().getColor(R.color.text_Red));;
            }

                card.total_equip_load = dustprotect + hirss + antiice;
                card.Total_load_Textview.setText(String.valueOf(card.total_equip_load)+ " kg.");
                card.landing_GW = GW - (int) Math.round(totalfuelconsump)+card.total_equip_load;
                card.textView_landing_GW.setText(String.valueOf(card.landing_GW) + " kg.");


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
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void update_static_weights(card_equipment_ViewHolder card) {
        dustprotect = Switch_to_integer(card.Switch_dustprotect.isChecked(), 200);
        hirss = Switch_to_integer(card.Switch_hirss.isChecked(), 300);
        antiice = Switch_to_integer(card.Switch_antiice.isChecked(), 800);
        updatefuelfield(card);
       /*
        static_weights = hoist + antiice + internaltank + bambibucket + armour + hirss + dustprotect;
        card.static_weight_Textview.setText(static_weights.toString() + " Kg.");
        update_GW(card0);
       */
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

    public class card0_arrival_ViewHolder extends ViewHolder {






        TextView

                arrival_altitude_TextView,
                arrival_fat_TextView,
                arrival_wind_TextView
               ;
        CheckBox checkBox_copyfromtakeoff;

        CheckBox arrival_Checkbox_useMeter, arrival_Checkbox_useMSec;
        Spinner arrival_Spinner_Wind_Direction;
        TextInputLayout textInputLayout_arrival_alt, textInputLayout_arrival_wind, textInputLayout_flight_time;

        public card0_arrival_ViewHolder(View v) {
            super(v);
            arrival_altitude_TextView = v.findViewById(R.id.Altitude_text);
            arrival_fat_TextView = v.findViewById(R.id.FATtext);
            arrival_wind_TextView = v.findViewById(R.id.windtext);
            arrival_Checkbox_useMeter = v.findViewById(R.id.checkBox_usemeter);
            arrival_Checkbox_useMSec = v.findViewById(R.id.checkBox_mtsec);
            arrival_Spinner_Wind_Direction = v.findViewById(R.id.spinner_wind_direction);


            textInputLayout_arrival_alt = v.findViewById(R.id.textInputLayout_Altitude_text);
            textInputLayout_arrival_wind = v.findViewById(R.id.textInputLayout_Wind);
            arrival_Spinner_Wind_Direction = v.findViewById(R.id.spinner_wind_direction);
            textInputLayout_flight_time = v.findViewById(R.id.textInputLayout_flight_time);
            checkBox_copyfromtakeoff = v.findViewById(R.id.checkBox_copyfromtakeoff);
            wind_direction = arrival_Spinner_Wind_Direction.getSelectedItem().toString();


        }
    }

    private class card1_arrival_ViewHolder extends ViewHolder {
        public Integer OGEWind;
        public Integer IGEWind;
        public int IgeTotal;
        public int OgeTotal;
        public Integer IGEtable = 0;
        public Integer OGETable = 0;



        TextView


                textViewOGEWind,
                textViewIGEWind,
                textViewIgeTotal,
                textViewOGETotal,
                textViewIGEtable,
                textViewOGEtable;


        public card1_arrival_ViewHolder(View v) {
            super(v);

            textViewIGEtable = v.findViewById(R.id.textViewIGEtable);
            textViewOGEtable = v.findViewById(R.id.textviewOGEtable);
            textViewOGEWind = v.findViewById(R.id.textViewOGEWing);
            textViewIGEWind = v.findViewById(R.id.textViewIGEWind);
            textViewIgeTotal = v.findViewById(R.id.textViewIgeTotal);
            textViewOGETotal = v.findViewById(R.id.textViewOGETotal);


        }
    }

    private class card2_arrival_ViewHolder extends ViewHolder {

        public double NTKfactor;
        double ntk25_1;
        double ntk60_2;
        double ntk60_1;
        double ntk30_1;
        double ntk30_2;


        TextView
                textView_25_1,
                textView_60_1,
                textView_60_2,
                textView_30_1,
                textView_NTKfactor,
                textView_30_2;


        public card2_arrival_ViewHolder(View v) {
            super(v);

            textView_25_1 = v.findViewById(R.id.textView_25_1);
            textView_60_1 = v.findViewById(R.id.textView_60_1);
            textView_60_2 = v.findViewById(R.id.textView_60_2);
            textView_30_1 = v.findViewById(R.id.textView_30_1);
            textView_30_2 = v.findViewById(R.id.textView_30_2);
            textView_NTKfactor = v.findViewById(R.id.textView_NTKfactor);

        }
    }

    private class card_equipment_ViewHolder extends ViewHolder {
        public int landing_GW = 0;
        public int total_equip_load = 0;
        Switch Switch_dustprotect,
                Switch_hirss,
                Switch_antiice;



        TextView
        Total_load_Textview,
        textView_fuel,
        textView_landing_GW,
        textView_takeoff_GW,
        textView_consumption,
        textView_ftimemin;

        EditText editText_ftime;

        public card_equipment_ViewHolder(View v) {
            super(v);
            Switch_dustprotect = v.findViewById(R.id.SwitchDust);
            Switch_hirss = v.findViewById(R.id.SwitchHirss);
            Switch_antiice = v.findViewById(R.id.SwitchAntiice);

            Total_load_Textview = v.findViewById(R.id.total_load_Textview);

            textView_ftimemin = v.findViewById(R.id.textView_ftimemin);
            textView_landing_GW = v.findViewById(R.id.textView_landing_GW);
            textView_fuel = v.findViewById(R.id.textView_fuel);
            textView_takeoff_GW = v.findViewById(R.id.textView_takeoff_GW);
            textView_consumption = v.findViewById(R.id.textView_consumption);
            editText_ftime = v.findViewById(R.id.editText_ftime);

        }

    }
}
