package com.ddcorp.mi_17helicopterperfomance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

class fragment_takeoff_recyler extends RecyclerView.Adapter<fragment_takeoff_recyler.ViewHolder> {
    public FragmentManager fragmentManager;
    public Integer dustprotect = 0, hoist = 0, hirss = 0, antiice = 0, internaltank = 0, bambibucket = 0, armour = 0;
    DatabaseAccess databaseAccess;
    Integer operating = 0, crew, fuel, load = 0, GW = 0;


    Integer OGETable = 0;
    Integer IGEtable = 0;
    Integer OGEWind = 0;
    Integer IGEWind = 0;
    Integer IgeTotal = 0;
    Integer OgeTotal = 0;





    Context context;
    Integer takeoff_alt , takeoff_fat ;
    Boolean fatisnull=true;

    Integer takeoff_alt_meter = 0;
    Integer takeoff_wind;
    Integer takeoff_wind_msec;
    String winddirection;

    card0_ViewHolder card0;

    private int card_0 = 0;
    private int card_equiptment = 1;
    private int card_1 = 2;
    private int card_2 = 3;
    private int card_3 = 4;
    private Integer total_GW;//GW+equipment load
    environmental_condition_listener environmental_condition_listener;
    private boolean noentry=true;

    public interface environmental_condition_listener{
        public void environmental_condition_listener(int altitude ,int fat,boolean usemeter, int wind_kt, int wind_direction, boolean usemsec);
    }


    public fragment_takeoff_recyler(Context context,environmental_condition_listener environmental_condition_listener) {
        this.context = context;
        // updateGrossWeight(GW, operating, crew, fuel, load, hoist, antiice, internaltank, bambibucket, armour);
        databaseAccess = DatabaseAccess.getInstance(context);
        this.environmental_condition_listener=environmental_condition_listener;



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
    public fragment_takeoff_recyler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        if (viewType == card_0) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_take_off_card0, parent, false);

            return new card0_ViewHolder(v);
        }
        if (viewType == card_equiptment) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_take_off_equipment_card, parent, false);

            return new card_equipment_ViewHolder(v);
        }
        if (viewType == card_1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_take_off_card1, parent, false);

            return new card1_ViewHolder(v);
        }
        if (viewType == card_2) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_take_off_card2, parent, false);

            return new card2_ViewHolder(v);
        }
        if (viewType == card_3) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_take_off_card3, parent, false);

            return new card3_ViewHolder(v);
        }


        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull fragment_takeoff_recyler.ViewHolder holder, int position) {
        if (holder.getItemViewType() == card_0) {
             card0 = (card0_ViewHolder) holder;

            if (card0.usemeter) {
                takeoff_alt_meter = Textview_to_integer(card0.Takeoff_altitude_TextView.getText().toString());
                takeoff_alt = (int) (Math.round(Double.valueOf(takeoff_alt_meter) * 3.2808399));
            } else {
                takeoff_alt = Textview_to_integer(card0.Takeoff_altitude_TextView.getText().toString());
                takeoff_alt_meter = (int) (Math.round(Double.valueOf(takeoff_alt) * 0.3048));
            }

            if (card0.usemsec) {
                takeoff_wind_msec = Textview_to_integer(card0.Takeoff_wind_TextView.getText().toString());
                takeoff_wind = (int) (Math.round(Double.valueOf(takeoff_wind_msec) * 1.94384449412));
            } else {
                takeoff_wind = Textview_to_integer(card0.Takeoff_wind_TextView.getText().toString());
                takeoff_wind_msec = (int) (Math.round(Double.valueOf(takeoff_wind) * 0.514444444));
            }

            takeoff_fat = FATTextview_to_integer(card0.Takeoff_fat_TextView.getText().toString());
//            arrival_alt = card.takeoff_alt_meter;
//            arrival_fat = card.arrival_fat;

            //  wind = Textview_to_integer(Takeoff_wind_TextView.getText().toString());
            card0.Takeoff_Spinner_Wind_Direction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    winddirection = card0.Takeoff_Spinner_Wind_Direction.getItemAtPosition(position).toString();
                    notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            card0.Takeoff_Checkbox_useMeter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (card0.Takeoff_Checkbox_useMeter.isChecked()) {
                        card0.usemeter = true;
                        card0.textInputLayout_takeoff_alt.setHint("Altitude (mt.)");
                        if (!card0.Takeoff_altitude_TextView.getText().toString().isEmpty()) {
                            takeoff_alt_meter = (int) (Math.round(Double.valueOf(takeoff_alt) * 0.3048));
                            card0.Takeoff_altitude_TextView.setText(takeoff_alt_meter.toString());
                        }
                    } else {
                        card0.usemeter = false;
                        card0.textInputLayout_takeoff_alt.setHint("Altitude (Ft.)");
                        if (!card0.Takeoff_altitude_TextView.getText().toString().isEmpty()) {
                            takeoff_alt = (int) (Math.round(Double.valueOf(takeoff_alt_meter) * 3.2808399));
                            card0.Takeoff_altitude_TextView.setText(takeoff_alt.toString());
                        }
                    }
                    environmental_condition_listener.environmental_condition_listener(takeoff_alt,takeoff_fat,card0.usemeter,takeoff_wind,card0.Takeoff_Spinner_Wind_Direction.getSelectedItemPosition(),card0.usemsec);

                }
            });
            card0.Takeoff_Checkbox_useMSec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (card0.Takeoff_Checkbox_useMSec.isChecked()) {
                        //wind = (int) (Math.round(Double.valueOf(wind) * 0.514444444));
                        card0.usemsec = true;
                        card0.textInputLayout_takeoff_wind.setHint("Wind (m/sec.)");
                        if (!card0.Takeoff_wind_TextView.getText().toString().isEmpty() && takeoff_wind > 0) {
                            card0.Takeoff_wind_TextView.setText(takeoff_wind_msec.toString());
                        }

                    } else {
                        // wind = (int) (Math.round(Double.valueOf(wind) * 1.94384449412));
                        card0.usemsec = false;
                        card0.textInputLayout_takeoff_wind.setHint("Wind (Kt.)");
                        if (!card0.Takeoff_wind_TextView.getText().toString().isEmpty() && takeoff_wind > 0) {
                            card0.Takeoff_wind_TextView.setText(takeoff_wind.toString());
                        }
                    }
                    environmental_condition_listener.environmental_condition_listener(takeoff_alt,takeoff_fat,card0.usemeter,takeoff_wind,card0.Takeoff_Spinner_Wind_Direction.getSelectedItemPosition(),card0.usemsec);

                }
            });

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!card0.Takeoff_altitude_TextView.getText().toString().isEmpty() && !card0.Takeoff_fat_TextView.getText().toString().isEmpty()) {
                        noentry=false;
                    }else noentry=true;

                    hidesoftkeyboard(v);
                    notifyDataSetChanged();

                }
            };
            card0.Takeoff_altitude_TextView.setOnClickListener(onClickListener);
            card0.Takeoff_fat_TextView.setOnClickListener(onClickListener);
            card0.Takeoff_wind_TextView.setOnClickListener(onClickListener);
            environmental_condition_listener.environmental_condition_listener(takeoff_alt,takeoff_fat,card0.usemeter,takeoff_wind,card0.Takeoff_Spinner_Wind_Direction.getSelectedItemPosition(),card0.usemsec);

        }
        if (holder.getItemViewType() == card_1) {
            final card1_ViewHolder card = (card1_ViewHolder) holder;
            if (!fatisnull && takeoff_alt>0) {
                IGEtable = databaseAccess.get_IGE_OGE_TABLE_value(takeoff_alt_meter, takeoff_fat, "ige");
                OGETable = databaseAccess.get_IGE_OGE_TABLE_value(takeoff_alt_meter, takeoff_fat, "oge");
                OGEWind = databaseAccess.getIGE_OGE_WING_value(takeoff_wind_msec, winddirection, "windIge");
                IGEWind = databaseAccess.getIGE_OGE_WING_value(takeoff_wind_msec, winddirection, "windOge");

                IgeTotal = IGEtable + IGEWind;
                OgeTotal = OGETable + OGEWind;
                if (IgeTotal > 13000) {
                    IgeTotal = 13000;
                }
                if (OgeTotal > 13000) {
                    OgeTotal = 13000;
                }

                if (GW == null) {
                    GW = 0;
                }
                int igehoverload = IgeTotal - total_GW;
                card.LoadforIGE_TextView.setText(String.valueOf(igehoverload) + "Kg.");//excel farkli -dustprotect
                int ogehoverload = OgeTotal - total_GW;
                card.LoadforOGE_TextView.setText(String.valueOf(ogehoverload) + "Kg.");


                if (igehoverload > 0) {
                    card.textView_IGEHOVERCAPABILITY.setText("IGE HOVER CAPABLE");
                    card.textView_IGEHOVERCAPABILITY.setBackground(context.getResources().getDrawable(R.drawable.textviewborder_green));
                    card.LoadforIGE_TextView.setTextColor(context.getResources().getColor(R.color.text_Green));
                    card.textView_IGEHOVERCAPABILITY.startAnimation(getBlinkAnimation());


                } else {
                    card.textView_IGEHOVERCAPABILITY.setText("IGE HOVER INCAPABLE");
                    card.textView_IGEHOVERCAPABILITY.setBackground(context.getResources().getDrawable(R.drawable.textviewborder_red));
                    card.LoadforIGE_TextView.setTextColor(context.getResources().getColor(R.color.text_Red));
                    card.textView_IGEHOVERCAPABILITY.startAnimation(getBlinkAnimation());

                }
                if (ogehoverload > 0) {
                    card.textView_OGEHOVERCAPABILITY.setText("OGE HOVER CAPABLE");
                    card.textView_OGEHOVERCAPABILITY.setBackground(context.getResources().getDrawable(R.drawable.textviewborder_green));
                    card.LoadforOGE_TextView.setTextColor(context.getResources().getColor(R.color.text_Green));
                    card.textView_OGEHOVERCAPABILITY.startAnimation(getBlinkAnimation());

                } else {
                    card.textView_OGEHOVERCAPABILITY.setText("OGE HOVER INCAPABLE");
                    card.textView_OGEHOVERCAPABILITY.setBackground(context.getResources().getDrawable(R.drawable.textviewborder_red));
                    card.LoadforOGE_TextView.setTextColor(context.getResources().getColor(R.color.text_Red));
                    card.textView_OGEHOVERCAPABILITY.startAnimation(getBlinkAnimation());

                }


            }

       /*     update_startdata();
            update_limited_operation();
            update_airspeed_fuel_singleeng();
*/
        }
        if (holder.getItemViewType() == card_2) {
            final card2_ViewHolder card = (card2_ViewHolder) holder;

            card.textViewIGEtable.setText(IGEtable.toString());
            card.textViewOGEtable.setText(OGETable.toString());
            card.textViewOGEWind.setText(OGEWind.toString());
            card.textViewIGEWind.setText(IGEWind.toString());
            card.textViewIgeTotal.setText(String.valueOf(IgeTotal));
            card.textViewOGETotal.setText(String.valueOf(OgeTotal));

       /*     update_startdata();
            update_limited_operation();
            update_airspeed_fuel_singleeng();
*/
        }
        if (holder.getItemViewType() == card_3) {
            final card3_ViewHolder card1_takeoff = (card3_ViewHolder) holder;
            if (!fatisnull && takeoff_alt>0) {
                card1_takeoff.NTKfactor = Double.valueOf(takeoff_alt_meter) / 1000 * 1.3;
                card1_takeoff.ntk60_1 = limitedoperation_NTK_calc(takeoff_fat, card1_takeoff.NTKfactor, 0.153, 91.0, 0.137, 92.9, 101.15, 0.0);
                card1_takeoff.ntk60_2 = limitedoperation_NTK_calc(takeoff_fat, card1_takeoff.NTKfactor, 0.167, 95.7, 0.137, 95.7, 101.15, 0.0);
                card1_takeoff.ntk30_1 = card1_takeoff.ntk60_2;
                //EĞER((EĞER(S5<0;((S5*0,167)+95,7+AD21);((S5*0,137)+95,7+AD21))+1,02)<101,15;(EĞER(S5<0;((S5*0,167)+95,7+AD21);((S5*0,137)+95,7+AD21))+1,02);101,15)
                card1_takeoff.ntk30_2 = limitedoperation_NTK_calc(takeoff_fat, card1_takeoff.NTKfactor, 0.167, 95.7, 0.137, 95.7, 101.15, 1.02);
                card1_takeoff.ntk25_1 = card1_takeoff.ntk30_2;

                card1_takeoff.textView_25_1.setText(new DecimalFormat("###.##").format(card1_takeoff.ntk25_1));
                card1_takeoff.textView_60_1.setText(new DecimalFormat("###.##").format(card1_takeoff.ntk60_1));
                card1_takeoff.textView_60_2.setText(new DecimalFormat("###.##").format(card1_takeoff.ntk60_2));
                card1_takeoff.textView_30_1.setText(new DecimalFormat("###.##").format(card1_takeoff.ntk30_1));
                card1_takeoff.textView_30_2.setText(new DecimalFormat("###.##").format(card1_takeoff.ntk30_2));
            }



       /*     update_startdata();
            update_limited_operation();
            update_airspeed_fuel_singleeng();
*/
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
            update_static_weights(card);

        }
       /* if (position+1 == getItemCount()) {
            notifyDataSetChanged();
        }
*/


    }

    private void update_static_weights(card_equipment_ViewHolder card) {
        dustprotect = Switch_to_integer(card.Switch_dustprotect.isChecked(), 200);
        hirss = Switch_to_integer(card.Switch_hirss.isChecked(), 300);
        antiice = Switch_to_integer(card.Switch_antiice.isChecked(), 800);
        total_GW=GW+dustprotect+hirss+antiice;
        card.Gw_Textview.setText(String.valueOf(GW) +" kg.");
        card.Total_load_Textview.setText(String.valueOf(dustprotect+hirss+antiice) +" kg.");
        card.Total_Gw_Textview.setText(String.valueOf(total_GW) +" kg.");

       /*
        static_weights = hoist + antiice + internaltank + bambibucket + armour + hirss + dustprotect;
        card.static_weight_Textview.setText(static_weights.toString() + " Kg.");
        update_GW(card0);
       */
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

        //EĞER((
        // EĞER(S5<0;((S5*0,167)+95,7+AD21);((S5*0,137)+95,7+AD21))+1,02)<101,15;
        // (EĞER(S5<0;((S5*0,167)+95,7+AD21);((S5*0,137)+95,7+AD21))+1,02);101,15)

        double returnvalue;
        if (cruise_fat < 0) {
            returnvalue = (cruise_fat * var1) + var2 + ntkfactor+ var6;
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
            if (!Text.isEmpty()) {
                returnvalue = Integer.parseInt(Text);
            }
        } catch (NumberFormatException e) {
            returnvalue = 0;
        }
        return returnvalue;
    }
    private Integer FATTextview_to_integer(String Text) {
        Integer returnvalue = 0;
        try {
            if (!Text.isEmpty()) {
                returnvalue = Integer.parseInt(Text);
                fatisnull=false;
            }else fatisnull=true;
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

    public void refresh(Integer gw, Integer operating, Integer crew, Integer fuel, Integer load, Integer hoist, Integer antiice, Integer internaltank, Integer bambibucket, Integer armour, Integer dustprotect) {
        this.GW = gw;
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
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

    public class card0_ViewHolder extends ViewHolder {

        TextView

                Takeoff_altitude_TextView,
                Takeoff_fat_TextView,
                Takeoff_wind_TextView;

        CheckBox Takeoff_Checkbox_useMeter, Takeoff_Checkbox_useMSec;
        Spinner Takeoff_Spinner_Wind_Direction;
        TextInputLayout textInputLayout_takeoff_alt, textInputLayout_takeoff_wind;
        private boolean usemeter;
        private boolean usemsec;


        public card0_ViewHolder(View v) {
            super(v);

            Takeoff_altitude_TextView = v.findViewById(R.id.Altitude_text);
            Takeoff_fat_TextView = v.findViewById(R.id.FATtext);
            Takeoff_wind_TextView = v.findViewById(R.id.windtext);
            Takeoff_Checkbox_useMeter = v.findViewById(R.id.checkBox_usemeter);
            Takeoff_Checkbox_useMSec = v.findViewById(R.id.checkBox_mtsec);
            Takeoff_Spinner_Wind_Direction = v.findViewById(R.id.spinner_wind_direction);

            textInputLayout_takeoff_alt = v.findViewById(R.id.textInputLayout_Altitude_text);
            textInputLayout_takeoff_wind = v.findViewById(R.id.textInputLayout_Wind);

        }
    }
    private class card1_ViewHolder extends ViewHolder {

        View view;


        TextView textView_caption;
        TextView
                LoadforIGE_TextView,
                LoadforOGE_TextView,
                textView_OGEHOVERCAPABILITY,
                textView_IGEHOVERCAPABILITY;












        public card1_ViewHolder(View v) {
            super(v);
            this.view = v;


            LoadforIGE_TextView = v.findViewById(R.id.textViewLoadforIGE);
            LoadforOGE_TextView = v.findViewById(R.id.textViewLoadforOGE);



            textView_OGEHOVERCAPABILITY = v.findViewById(R.id.textView_OGEHOVERCAPABILITY);
            textView_IGEHOVERCAPABILITY = v.findViewById(R.id.textView_IGEHOVERCAPABILITY);

            textView_caption = v.findViewById(R.id.textView_caption);

        }
    }
    private class card2_ViewHolder extends ViewHolder {


        View view;




        TextView
                textViewIGEtable,
                textViewOGEtable,
                textViewIgeTotal,
                textViewOGETotal,
                textViewOGEWind,
                textViewIGEWind
                ;



        public card2_ViewHolder(View v) {
            super(v);
            this.view = v;


            textViewIGEtable = v.findViewById(R.id.textViewIGEtable);
            textViewOGEtable = v.findViewById(R.id.textviewOGEtable);
            textViewIgeTotal = v.findViewById(R.id.textViewIgeTotal);
            textViewOGETotal = v.findViewById(R.id.textViewOGETotal);
            textViewOGEWind = v.findViewById(R.id.textViewOGEWing);
            textViewIGEWind = v.findViewById(R.id.textViewIGEWind);




        }
    }
    private class card3_ViewHolder extends ViewHolder {
        View view;
         double ntk60_1 = 0;
        double ntk60_2 = 0;
        double ntk30_1 = 0;
        double ntk30_2 = 0;
        double ntk25_1 = 0;
        double NTKfactor;


        TextView

                textView_25_1,
                textView_60_1,
                textView_60_2,
                textView_30_1,
                textView_30_2
               ;



        public card3_ViewHolder(View v) {
            super(v);
            this.view = v;
            textView_25_1 = v.findViewById(R.id.textView_25_1);
            textView_60_1 = v.findViewById(R.id.textView_60_1);
            textView_60_2 = v.findViewById(R.id.textView_60_2);
            textView_30_1 = v.findViewById(R.id.textView_30_1);
            textView_30_2 = v.findViewById(R.id.textView_30_2);


        }
    }
    private class card_equipment_ViewHolder extends ViewHolder {
        Switch Switch_dustprotect,
                Switch_hirss,
                Switch_antiice;
        TextView Gw_Textview;
        TextView Total_Gw_Textview;
        TextView Total_load_Textview;
        public card_equipment_ViewHolder(View v) {
            super(v);
            Switch_dustprotect = v.findViewById(R.id.SwitchDust);
            Switch_hirss = v.findViewById(R.id.SwitchHirss);
            Switch_antiice = v.findViewById(R.id.SwitchAntiice);
            Gw_Textview = v.findViewById(R.id.Gw_Textview);
            Total_Gw_Textview = v.findViewById(R.id.Total_gw_Textview);
            Total_load_Textview = v.findViewById(R.id.total_load_Textview);

        }
    }
}
