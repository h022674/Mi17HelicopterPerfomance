package com.ddcorp.mi_17helicopterperfomance;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
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
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

import static com.ddcorp.mi_17helicopterperfomance.MainActivity.TAG;

class mRecylerAdapter extends RecyclerView.Adapter<mRecylerAdapter.ViewHolder> {
    public FragmentManager fragmentManager;
    public Integer dustprotect = 0, hoist = 0, hirss = 0, antiice = 0, internaltank = 0, bambibucket = 0, armour = 0;
    DatabaseAccess databaseAccess;
    Integer operating=0, crew, fuel, load=0, GW = 0;

    Integer singleeng25;
    Integer vmax;
    Integer vmin;
    Integer fuelconsumption;
    Integer singleeng30;

    Context context;
    int takeoff_alt = 0, takeoff_fat = 0;
    int measuredHeight;
    private int card1_takeoff = 0;
    private int card2_start = 1;
    private int card3_cruise = 2;
    private int card4_arrival = 3;

    public mRecylerAdapter(Context context) {
        this.context = context;
        // updateGrossWeight(GW, operating, crew, fuel, load, hoist, antiice, internaltank, bambibucket, armour);
        databaseAccess = DatabaseAccess.getInstance(context);


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
    public mRecylerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;
        if (viewType == card1_takeoff) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_take_off_card0, parent, false);

            return new cardtakeoff_ViewHolder(v);
        }
        if (viewType == card2_start) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.start_card, parent, false);

            return new cardstart_ViewHolder(v);
        }

        if (viewType == card3_cruise) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_cruise_card, parent, false);

            return new cardcruise_ViewHolder(v);
        }
        if (viewType == card4_arrival) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_arrival_card, parent, false);

            return new cardarrival_ViewHolder(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull mRecylerAdapter.ViewHolder holder, int position) {
        if (holder.getItemViewType() == card1_takeoff) {
            final cardtakeoff_ViewHolder card1_takeoff = (cardtakeoff_ViewHolder) holder;
            if (load > 0 || operating > 0) {
                card1_takeoff.textView_caption_takeoffcard.setEnabled(true);
            } else {
                card1_takeoff.textView_caption_takeoffcard.setEnabled(false);
                checkstate(card1_takeoff);
            }

            card1_takeoff.textView_caption_takeoffcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkstate(card1_takeoff);

                }


            });
            card1_takeoff.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.clearFocus();
                }
            });

            if (card1_takeoff.usemeter) {
                card1_takeoff.takeoff_alt_meter = Textview_to_integer(card1_takeoff.Takeoff_altitude_TextView.getText().toString());
                card1_takeoff.takeoff_alt = (int) (Math.round(Double.valueOf(card1_takeoff.takeoff_alt_meter) * 3.2808399));
            } else {
                card1_takeoff.takeoff_alt = Textview_to_integer(card1_takeoff.Takeoff_altitude_TextView.getText().toString());
                card1_takeoff.takeoff_alt_meter = (int) (Math.round(Double.valueOf(card1_takeoff.takeoff_alt) * 0.3048));
            }

            if (card1_takeoff.usemsec) {
                card1_takeoff.takeoff_wind_msec = Textview_to_integer(card1_takeoff.Takeoff_wind_TextView.getText().toString());
                card1_takeoff.takeoff_wind = (int) (Math.round(Double.valueOf(card1_takeoff.takeoff_wind_msec) * 1.94384449412));
            } else {
                card1_takeoff.takeoff_wind = Textview_to_integer(card1_takeoff.Takeoff_wind_TextView.getText().toString());
                card1_takeoff.takeoff_wind_msec = (int) (Math.round(Double.valueOf(card1_takeoff.takeoff_wind) * 0.514444444));
            }

            card1_takeoff.takeoff_fat = Textview_to_integer(card1_takeoff.Takeoff_fat_TextView.getText().toString());
            takeoff_alt = card1_takeoff.takeoff_alt_meter;
            takeoff_fat = card1_takeoff.takeoff_fat;

            //  wind = Textview_to_integer(Takeoff_wind_TextView.getText().toString());


            if (!card1_takeoff.Takeoff_fat_TextView.getText().toString().isEmpty() && !card1_takeoff.Takeoff_altitude_TextView.getText().toString().isEmpty()) {
                card1_takeoff.IGEtable = databaseAccess.get_IGE_OGE_TABLE_value(card1_takeoff.takeoff_alt_meter, card1_takeoff.takeoff_fat, "ige");
                card1_takeoff.IGEtable = card1_takeoff.IGEtable - dustprotect - antiice - hirss;

                card1_takeoff.OGETable = databaseAccess.get_IGE_OGE_TABLE_value(card1_takeoff.takeoff_alt_meter, card1_takeoff.takeoff_fat, "oge");
                card1_takeoff.OGETable = card1_takeoff.OGETable - dustprotect - antiice - hirss;

                card1_takeoff.OGEWind = databaseAccess.getIGE_OGE_WING_value(card1_takeoff.takeoff_wind_msec, card1_takeoff.Takeoff_Spinner_Wind_Direction.getSelectedItem().toString(), "windIge");
                card1_takeoff.IGEWind = databaseAccess.getIGE_OGE_WING_value(card1_takeoff.takeoff_wind_msec, card1_takeoff.Takeoff_Spinner_Wind_Direction.getSelectedItem().toString(), "windOge");

                card1_takeoff.IgeTotal = card1_takeoff.IGEtable + card1_takeoff.IGEWind;
                card1_takeoff.OgeTotal = card1_takeoff.OGETable + card1_takeoff.OGEWind;
                if (card1_takeoff.IgeTotal > 13000) {
                    card1_takeoff.IgeTotal = 13000;
                }
                if (card1_takeoff.OgeTotal > 13000) {
                    card1_takeoff.OgeTotal = 13000;
                }

                if (GW == null) {
                    GW = 0;
                }
                int igehoverload = card1_takeoff.IgeTotal - GW - dustprotect;
                card1_takeoff.LoadforIGE_TextView.setText(String.valueOf(igehoverload) + "Kg.");//excel farkli -dustprotect
                int ogehoverload = card1_takeoff.OgeTotal - GW - dustprotect;
                card1_takeoff.LoadforOGE_TextView.setText(String.valueOf(ogehoverload) + "Kg.");


                if (igehoverload > 0) {
                    card1_takeoff.textView_IGEHOVERCAPABILITY.setText("IGE HOVER CAPABLE");
                    card1_takeoff.textView_IGEHOVERCAPABILITY.setBackground(context.getResources().getDrawable(R.drawable.textviewborder_green));
                    card1_takeoff.LoadforIGE_TextView.setTextColor(context.getResources().getColor(R.color.text_Green));
                    card1_takeoff.textView_IGEHOVERCAPABILITY.startAnimation(getBlinkAnimation());


                } else {
                    card1_takeoff.textView_IGEHOVERCAPABILITY.setText("IGE HOVER INCAPABLE");
                    card1_takeoff.textView_IGEHOVERCAPABILITY.setBackground(context.getResources().getDrawable(R.drawable.textviewborder_red));
                    card1_takeoff.LoadforIGE_TextView.setTextColor(context.getResources().getColor(R.color.text_Red));
                    card1_takeoff.textView_IGEHOVERCAPABILITY.startAnimation(getBlinkAnimation());

                }
                if (ogehoverload > 0) {
                    card1_takeoff.textView_OGEHOVERCAPABILITY.setText("OGE HOVER CAPABLE");
                    card1_takeoff.textView_OGEHOVERCAPABILITY.setBackground(context.getResources().getDrawable(R.drawable.textviewborder_green));
                    card1_takeoff.LoadforOGE_TextView.setTextColor(context.getResources().getColor(R.color.text_Green));
                    card1_takeoff.textView_OGEHOVERCAPABILITY.startAnimation(getBlinkAnimation());

                } else {
                    card1_takeoff.textView_OGEHOVERCAPABILITY.setText("OGE HOVER INCAPABLE");
                    card1_takeoff.textView_OGEHOVERCAPABILITY.setBackground(context.getResources().getDrawable(R.drawable.textviewborder_red));
                    card1_takeoff.LoadforOGE_TextView.setTextColor(context.getResources().getColor(R.color.text_Red));
                    card1_takeoff.textView_OGEHOVERCAPABILITY.startAnimation(getBlinkAnimation());

                }


            }
            card1_takeoff.Takeoff_Spinner_Wind_Direction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            card1_takeoff.Takeoff_Checkbox_useMeter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (card1_takeoff.Takeoff_Checkbox_useMeter.isChecked()) {
                        card1_takeoff.usemeter = true;
                        // altitude_meter = (int) (Math.round(Double.valueOf(altitude_meter) * 0.3048));
                        card1_takeoff.textInputLayout_takeoff_alt.setHint("Altitude (mt.)");
                        if (!card1_takeoff.Takeoff_altitude_TextView.getText().toString().isEmpty() && card1_takeoff.takeoff_alt > 0) {
                            card1_takeoff.Takeoff_altitude_TextView.setText(card1_takeoff.takeoff_alt_meter.toString());
                        }
                    } else {
                        card1_takeoff.usemeter = false;
                        //  altitude_meter = (int) (Math.round(Double.valueOf(altitude_meter) * 3.2808399));
                        card1_takeoff.textInputLayout_takeoff_alt.setHint("Altitude (Ft.)");
                        if (!card1_takeoff.Takeoff_altitude_TextView.getText().toString().isEmpty() && card1_takeoff.takeoff_alt_meter > 0) {
                            card1_takeoff.Takeoff_altitude_TextView.setText(card1_takeoff.takeoff_alt.toString());
                        }
                    }
                }
            });
            card1_takeoff.Takeoff_Checkbox_useMSec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (card1_takeoff.Takeoff_Checkbox_useMSec.isChecked()) {
                        //wind = (int) (Math.round(Double.valueOf(wind) * 0.514444444));
                        card1_takeoff.textInputLayout_takeoff_wind.setHint("Wind (m/sec.)");
                        if (!card1_takeoff.Takeoff_wind_TextView.getText().toString().isEmpty() && card1_takeoff.takeoff_wind > 0) {
                            card1_takeoff.Takeoff_wind_TextView.setText(card1_takeoff.takeoff_wind_msec.toString());
                        }

                    } else {
                        // wind = (int) (Math.round(Double.valueOf(wind) * 1.94384449412));
                        card1_takeoff.textInputLayout_takeoff_wind.setHint("Wind (Kt.)");
                        if (!card1_takeoff.Takeoff_wind_TextView.getText().toString().isEmpty() && card1_takeoff.takeoff_wind > 0) {
                            card1_takeoff.Takeoff_wind_TextView.setText(card1_takeoff.takeoff_wind.toString());
                        }
                    }

                }
            });

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                    hidesoftkeyboard(v);
                }
            };
            card1_takeoff.Takeoff_altitude_TextView.setOnClickListener(onClickListener);
            card1_takeoff.Takeoff_fat_TextView.setOnClickListener(onClickListener);
            card1_takeoff.Takeoff_wind_TextView.setOnClickListener(onClickListener);

            card1_takeoff.IGEtable = databaseAccess.get_IGE_OGE_TABLE_value(card1_takeoff.takeoff_alt_meter, takeoff_fat, "ige");
            card1_takeoff.IGEtable = card1_takeoff.IGEtable - dustprotect - antiice - hirss;
            card1_takeoff.textViewIGEtable.setText(card1_takeoff.IGEtable.toString());

            card1_takeoff.OGETable = databaseAccess.get_IGE_OGE_TABLE_value(card1_takeoff.takeoff_alt_meter, takeoff_fat, "oge");
            card1_takeoff.OGETable = card1_takeoff.OGETable - dustprotect - antiice - hirss;
            card1_takeoff.textViewOGEtable.setText(card1_takeoff.OGETable.toString());


            card1_takeoff.OGEWind = databaseAccess.getIGE_OGE_WING_value(card1_takeoff.takeoff_wind_msec, card1_takeoff.Takeoff_Spinner_Wind_Direction.getSelectedItem().toString(), "windIge");
            card1_takeoff.IGEWind = databaseAccess.getIGE_OGE_WING_value(card1_takeoff.takeoff_wind_msec, card1_takeoff.Takeoff_Spinner_Wind_Direction.getSelectedItem().toString(), "windOge");
            card1_takeoff.textViewOGEWind.setText(card1_takeoff.OGEWind.toString());
            card1_takeoff.textViewIGEWind.setText(card1_takeoff.IGEWind.toString());


            card1_takeoff.IgeTotal = card1_takeoff.IGEtable + card1_takeoff.IGEWind;
            card1_takeoff.OgeTotal = card1_takeoff.OGETable + card1_takeoff.OGEWind;
            if (card1_takeoff.IgeTotal > 13000) {
                card1_takeoff.IgeTotal = 13000;
            }
            if (card1_takeoff.OgeTotal > 13000) {
                card1_takeoff.OgeTotal = 13000;
            }
            card1_takeoff.textViewIgeTotal.setText(String.valueOf(card1_takeoff.IgeTotal));
            card1_takeoff.textViewOGETotal.setText(String.valueOf(card1_takeoff.OgeTotal));
            card1_takeoff.NTKfactor = Double.valueOf(card1_takeoff.takeoff_alt_meter) / 1000 * 1.3;
            card1_takeoff.ntk60_1 = limitedoperation_NTK_calc(card1_takeoff.takeoff_fat, card1_takeoff.NTKfactor, 0.153, 91.0, 0.137, 92.9, 101.15, 0.0);
            card1_takeoff.ntk60_2 = limitedoperation_NTK_calc(card1_takeoff.takeoff_fat, card1_takeoff.NTKfactor, 0.167, 95.7, 0.137, 95.7, 101.15, 0.0);
            card1_takeoff.ntk30_1 = card1_takeoff.ntk60_2;
            card1_takeoff.ntk30_2 = limitedoperation_NTK_calc(card1_takeoff.takeoff_fat, card1_takeoff.NTKfactor, 0.167, 95.7, 0.137, 95.7, 101.15, 1.02);
            card1_takeoff.ntk25_1 = card1_takeoff.ntk30_2;

            card1_takeoff.textView_25_1.setText(new DecimalFormat("###.##").format(card1_takeoff.ntk25_1));
            card1_takeoff.textView_60_1.setText(new DecimalFormat("###.##").format(card1_takeoff.ntk60_1));
            card1_takeoff.textView_60_2.setText(new DecimalFormat("###.##").format(card1_takeoff.ntk60_2));
            card1_takeoff.textView_30_1.setText(new DecimalFormat("###.##").format(card1_takeoff.ntk30_1));
            card1_takeoff.textView_30_2.setText(new DecimalFormat("###.##").format(card1_takeoff.ntk30_2));







       /*     update_startdata();
            update_limited_operation();
            update_airspeed_fuel_singleeng();
*/
        }
        if (holder.getItemViewType() == card2_start) {
            final cardstart_ViewHolder card2_start = (cardstart_ViewHolder) holder;
            if (takeoff_alt > 0 || takeoff_fat > 0) {
                card2_start.textView_caption_startcard.setEnabled(true);

            } else {
                card2_start.textView_caption_startcard.setEnabled(false);

            }
            checkstate(card2_start);
            card2_start.textView_caption_startcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkstate(card2_start);
                }
            });


            //=((altitude_meter*0,0046)*-1)+(1,97-((fat^1,007)/6200))
            card2_start.apuairpress = ((takeoff_fat * 0.0046) * -1) + (1.97 - (Math.pow(takeoff_fat, 1.007) / 6200));
            //=EĞER(S5<=13;((S5*2,5)+750);780)
            if (takeoff_fat <= 13) {
                card2_start.maxtit = (takeoff_fat * 2.5) + 750;
            } else {
                card2_start.maxtit = 780;
            }
            //=EĞER(S5<=6;((S5-5)*0,192)+72;((S5-5)*0,0254)+72)
            if (takeoff_fat <= 6) {
                card2_start.idlentk_1 = ((takeoff_fat - 5) * 0.192) + 72;
            } else {
                card2_start.idlentk_1 = ((takeoff_fat - 5) * 0.0254) + 72;
            }

            card2_start.idlentk_2 = card2_start.idlentk_1 + 6;

            //=EĞER(S5<=38;((S5*4,1)+785);985
            if (takeoff_fat <= 38) {
                card2_start.acctit = (takeoff_fat * 4.1) + 785;
            } else {
                card2_start.acctit = 985;
            }

            //=(N5/1000)*1,3
            card2_start.ntkfactor = (Double.valueOf(takeoff_alt) / 1000) * 1.3;


            card2_start.textView_apuairpress.setText(new DecimalFormat("#.##").format(card2_start.apuairpress));
            card2_start.textView_maxtit.setText(new DecimalFormat("###").format(card2_start.maxtit));
            card2_start.textView_idlentk1.setText(new DecimalFormat("##.##").format(card2_start.idlentk_1));
            card2_start.textView_idlentk2.setText(new DecimalFormat("##.##").format(card2_start.idlentk_2));
            card2_start.textView_acctit.setText(new DecimalFormat("###").format(card2_start.acctit));
            card2_start.textView_ntkfactor.setText(new DecimalFormat("#.##").format(card2_start.ntkfactor));


        }
        if (holder.getItemViewType() == card3_cruise) {
            final cardcruise_ViewHolder card3_cruise = (cardcruise_ViewHolder) holder;
            if (takeoff_alt > 0 || takeoff_fat > 0) {
                card3_cruise.textView_caption_cruisecard.setEnabled(true);

            } else {
                card3_cruise.textView_caption_cruisecard.setEnabled(false);

            }
            checkstate(card3_cruise);
            card3_cruise.textView_caption_cruisecard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkstate(card3_cruise);
                }
            });

            card3_cruise.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.clearFocus();
                }
            });

            if (card3_cruise.usemeter) {
                card3_cruise.Cruise_alt_meter = Textview_to_integer(card3_cruise.Cruise_altitude_TextView.getText().toString());
                card3_cruise.Cruise_alt = (int) (Math.round(Double.valueOf(card3_cruise.Cruise_alt_meter) * 3.2808399));
            } else {
                card3_cruise.Cruise_alt = Textview_to_integer(card3_cruise.Cruise_altitude_TextView.getText().toString());
                card3_cruise.Cruise_alt_meter = (int) (Math.round(Double.valueOf(card3_cruise.Cruise_alt) * 0.3048));
            }
            if (!card3_cruise.Cruise_altitude_TextView.getText().toString().isEmpty() && !card3_cruise.Cruise_fat_TextView.getText().toString().isEmpty()) {
                card3_cruise.NTKfactor = Double.valueOf(card3_cruise.Cruise_alt_meter) / 1000 * 1.3;
                card3_cruise.Cruise_fat = Textview_to_integer(card3_cruise.Cruise_fat_TextView.getText().toString());
                card3_cruise.cruise1_1 = limitedoperation_NTK_calc(card3_cruise.Cruise_fat, card3_cruise.NTKfactor, 0.153, 90, 0.153, 90, 101.15, 0);
                card3_cruise.cruise1_2 = card3_cruise.cruise2_1 = limitedoperation_NTK_calc(card3_cruise.Cruise_fat, card3_cruise.NTKfactor, 0.153, 91, 0.137, 91, 101.15, 0);
                //card2_start.cruise2_1 = card2_start.cruise1_2;
                card3_cruise.cruise2_2 = limitedoperation_NTK_calc(card3_cruise.Cruise_fat, card3_cruise.NTKfactor, 0.153, 91, 0.137, 92.9, 101.15, 0);
                //=(M59/1000)*1,3


                card3_cruise.textView_cruise1_1.setText(new DecimalFormat("###.##").format(card3_cruise.cruise1_1));
                card3_cruise.textView_cruise1_2.setText(new DecimalFormat("###.##").format(card3_cruise.cruise1_2));
                card3_cruise.textView_cruise2_1.setText(new DecimalFormat("###.##").format(card3_cruise.cruise2_1));
                card3_cruise.textView_cruise2_2.setText(new DecimalFormat("###.##").format(card3_cruise.cruise2_2));
                card3_cruise.textView_NTKfactor.setText(new DecimalFormat("#.##").format(card3_cruise.NTKfactor));
            }
            card3_cruise.Cruise_Checkbox_useMeter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (card3_cruise.Cruise_Checkbox_useMeter.isChecked()) {
                        card3_cruise.usemeter = true;
                        // Cruise_alt_meter = (int) (Math.round(Double.valueOf(Cruise_alt) * 0.3048));
                        card3_cruise.textInputLayout_Cruise_alt.setHint("Altitude (mt.)");
                        if (!card3_cruise.Cruise_altitude_TextView.getText().toString().isEmpty() && card3_cruise.Cruise_alt > 0) {
                            card3_cruise.Cruise_altitude_TextView.setText(card3_cruise.Cruise_alt_meter.toString());
                        }
                    } else {
                        card3_cruise.usemeter = false;
                        //  Cruise_alt = (int) (Math.round(Double.valueOf(Cruise_alt_meter) * 3.2808399));
                        card3_cruise.textInputLayout_Cruise_alt.setHint("Altitude (Ft.)");
                        if (!card3_cruise.Cruise_altitude_TextView.getText().toString().isEmpty() && card3_cruise.Cruise_alt_meter > 0) {
                            card3_cruise.Cruise_altitude_TextView.setText(card3_cruise.Cruise_alt.toString());
                        }
                    }
                }
            });
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                    hidesoftkeyboard(v);
                }
            };
            card3_cruise.Cruise_altitude_TextView.setOnClickListener(onClickListener);
            card3_cruise.Cruise_fat_TextView.setOnClickListener(onClickListener);
            if (card3_cruise.Cruise_fat > 0 && card3_cruise.Cruise_alt_meter > 0) {


                try {
                    singleeng25 = Integer.valueOf(card3_cruise.textView_singleeng25.getText().toString());
                    vmax = Integer.valueOf(card3_cruise.textView_vmax.getText().toString());
                    vmin = Integer.valueOf(card3_cruise.textView_vmin.getText().toString());
                    fuelconsumption = Integer.valueOf(card3_cruise.textView_fuel.getText().toString());
                    singleeng30 = Integer.valueOf(card3_cruise.textView_singleeng30.getText().toString());

                } catch (Exception e) {
                    Log.i(TAG, "update_airspeed_fuel_singleeng: exception ", e);
                }
                if (GW > 11100) {
                    vmax = databaseAccess.get_v_speeds(takeoff_alt, "ustmax");
                    vmin = databaseAccess.get_v_speeds(takeoff_alt, "ustmin");

                } else {
                    vmax = databaseAccess.get_v_speeds(takeoff_alt, "altmax");
                    vmin = databaseAccess.get_v_speeds(takeoff_alt, "altmin");
                }

                singleeng30 = databaseAccess.get_singleeng(takeoff_alt, takeoff_fat, "singleeng30");
                singleeng25 = databaseAccess.get_singleeng(takeoff_alt, takeoff_fat, "sineng25");
                fuelconsumption = databaseAccess.get_fuelconsumption(GW, card3_cruise.Cruise_alt_meter);


                card3_cruise.textView_vmax.setText(vmax.toString());
                card3_cruise.textView_vmin.setText(vmin.toString());
                card3_cruise.textView_singleeng25.setText(singleeng25.toString());
                card3_cruise.textView_singleeng30.setText(singleeng30.toString());
                card3_cruise.textView_fuel.setText(fuelconsumption.toString());
            }


        }
        if (holder.getItemViewType() == card4_arrival) {



            final cardarrival_ViewHolder card4_arrival = (cardarrival_ViewHolder) holder;
            if (takeoff_alt > 0 || takeoff_fat > 0) {
                card4_arrival.textView_caption_arrivalcard.setEnabled(true);

            } else {
                card4_arrival.textView_caption_arrivalcard.setEnabled(false);

            }
            checkstate(card4_arrival);
            card4_arrival.textView_caption_arrivalcard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkstate(card4_arrival);


                }
            });
            card4_arrival.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.clearFocus();
                }
            });
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyDataSetChanged();
                    hidesoftkeyboard(v);
                }
            };
            card4_arrival.arrival_altitude_TextView.setOnClickListener(onClickListener);
            card4_arrival.arrival_fat_TextView.setOnClickListener(onClickListener);
            card4_arrival.arrival_wind_TextView.setOnClickListener(onClickListener);
            card4_arrival.textViewFlighttime.setOnClickListener(onClickListener);

            if (card4_arrival.usemeter) {
                card4_arrival.arrival_alt_meter = Textview_to_integer(card4_arrival.arrival_altitude_TextView.getText().toString());
                card4_arrival.arrival_alt = (int) (Math.round(Double.valueOf(card4_arrival.arrival_alt_meter) * 3.2808399));
            } else {
                card4_arrival.arrival_alt = Textview_to_integer(card4_arrival.arrival_altitude_TextView.getText().toString());
                card4_arrival.arrival_alt_meter = (int) (Math.round(Double.valueOf(card4_arrival.arrival_alt) * 0.3048));
            }

            if (card4_arrival.usemsec) {
                card4_arrival.arrival_wind_msec = Textview_to_integer(card4_arrival.arrival_wind_TextView.getText().toString());
                card4_arrival.arrival_wind = (int) (Math.round(Double.valueOf(card4_arrival.arrival_wind_msec) * 1.94384449412));
            } else {
                card4_arrival.arrival_wind = Textview_to_integer(card4_arrival.arrival_wind_TextView.getText().toString());
                card4_arrival.arrival_wind_msec = (int) (Math.round(Double.valueOf(card4_arrival.arrival_wind) * 0.514444444));
            }

            card4_arrival.arrival_Checkbox_useMeter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (card4_arrival.arrival_Checkbox_useMeter.isChecked()) {
                        card4_arrival.usemeter = true;
                        // arrival_alt_meter = (int) (Math.round(Double.valueOf(arrival_alt) * 0.3048));
                        card4_arrival.textInputLayout_arrival_alt.setHint("Altitude (mt.)");
                        if (!card4_arrival.arrival_altitude_TextView.getText().toString().isEmpty() && card4_arrival.arrival_alt > 0) {
                            card4_arrival.arrival_altitude_TextView.setText(card4_arrival.arrival_alt_meter.toString());
                        }
                    } else {
                        card4_arrival.usemeter = false;
                        //  arrival_alt = (int) (Math.round(Double.valueOf(arrival_alt_meter) * 3.2808399));
                        card4_arrival.textInputLayout_arrival_alt.setHint("Altitude (Ft.)");
                        if (!card4_arrival.arrival_altitude_TextView.getText().toString().isEmpty() && card4_arrival.arrival_alt_meter > 0) {
                            card4_arrival.arrival_altitude_TextView.setText(card4_arrival.arrival_alt.toString());
                        }
                    }
                }
            });

            card4_arrival.arrival_Checkbox_useMSec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (card4_arrival.arrival_Checkbox_useMSec.isChecked()) {
                        //arrival_wind = (int) (Math.round(Double.valueOf(arrival_wind) * 0.514444444));
                        card4_arrival.textInputLayout_arrival_wind.setHint("Wind (m/sec.)");
                        if (!card4_arrival.arrival_wind_TextView.getText().toString().isEmpty() && card4_arrival.arrival_wind > 0) {
                            card4_arrival.arrival_wind_TextView.setText(card4_arrival.arrival_wind_msec.toString());
                        }

                    } else {
                        // arrival_wind = (int) (Math.round(Double.valueOf(arrival_wind) * 1.94384449412));
                        card4_arrival.textInputLayout_arrival_wind.setHint("Wind (Kt.)");
                        if (!card4_arrival.arrival_wind_TextView.getText().toString().isEmpty() && card4_arrival.arrival_wind > 0) {
                            card4_arrival.arrival_wind_TextView.setText(card4_arrival.arrival_wind.toString());
                        }
                    }

                }
            });

            card4_arrival.arrival_Spinner_Wind_Direction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            Integer ftime = getIntegerformat(card4_arrival.textViewFlighttime.getText().toString());
            if (ftime > 0) {
                card4_arrival.textView_ftimemin.setText("(" + ftime.toString() + " min.)");
            } else //card3_cruise.textView_ftimemin.setText("(invalid)");

                if (!card4_arrival.arrival_fat_TextView.getText().toString().isEmpty() && !card4_arrival.arrival_altitude_TextView.getText().toString().isEmpty()) {

                    card4_arrival.OGEWind = databaseAccess.getIGE_OGE_WING_value(card4_arrival.arrival_wind_msec, card4_arrival.arrival_Spinner_Wind_Direction.getSelectedItem().toString(), "windIge");
                    card4_arrival.IGEWind = databaseAccess.getIGE_OGE_WING_value(card4_arrival.arrival_wind_msec, card4_arrival.arrival_Spinner_Wind_Direction.getSelectedItem().toString(), "windOge");
                    card4_arrival.textViewOGEWind.setText(card4_arrival.OGEWind.toString());
                    card4_arrival.textViewIGEWind.setText(card4_arrival.IGEWind.toString());

                    card4_arrival.IgeTotal = card4_arrival.IGEtable + card4_arrival.IGEWind;
                    card4_arrival.OgeTotal = card4_arrival.OGETable + card4_arrival.OGEWind;
                    if (card4_arrival.IgeTotal > 13000) {
                        card4_arrival.IgeTotal = 13000;
                    }
                    if (card4_arrival.OgeTotal > 13000) {
                        card4_arrival.OgeTotal = 13000;
                    }
                    card4_arrival.textViewIgeTotal.setText(String.valueOf(card4_arrival.IgeTotal));
                    card4_arrival.textViewOGETotal.setText(String.valueOf(card4_arrival.OgeTotal));


                    if (!card4_arrival.textViewFlighttime.getText().toString().isEmpty() && fuelconsumption > 0 && GW > 0) {
                        card4_arrival.landing_GW = GW - (fuelconsumption / 60 * ftime);
                        card4_arrival.textView_landing_GW.setText(String.valueOf(card4_arrival.landing_GW));
                    }


                    card4_arrival.NTKfactor = Double.valueOf(card4_arrival.arrival_alt_meter) / 1000 * 1.3;
                    card4_arrival.arrival_fat = Textview_to_integer(card4_arrival.arrival_fat_TextView.getText().toString());
                    //EĞER(EĞER(R74<0;((R74*0,153)+91+Y87);((R74*0,137)+92,9+Y87))<101,15;EĞER(R74<0;((R74*0,153)+91+Y87);((R74*0,137)+92,9+Y87));101,15)
                    card4_arrival.ntk60_1 = limitedoperation_NTK_calc(card4_arrival.arrival_fat, card4_arrival.NTKfactor, 0.153, 91, 0.137, 92.9, 101.15, 0);
                    //=EĞER(EĞER(R74<0;((R74*0,167)+95,7+Y87);((R74*0,137)+95,7+Y87))<101,15;EĞER(R74<0;((R74*0,167)+95,7+Y87);((R74*0,137)+95,7+Y87));101,15)
                    card4_arrival.ntk60_2 = limitedoperation_NTK_calc(card4_arrival.arrival_fat, card4_arrival.NTKfactor, 0.167, 95.7, 0.137, 95.7, 101.15, 0);
                    card4_arrival.ntk30_1 = card4_arrival.ntk60_2;
                    //=EĞER((EĞER(R74<0;((R74*0,167)+95,7+Y87);((R74*0,137)+95,7+Y87))+1,02)<101,15;(EĞER(R74<0;((R74*0,167)+95,7+Y87);((R74*0,137)+95,7+Y87))+1,02);101,15)
                    card4_arrival.ntk30_2 = limitedoperation_NTK_calc(card4_arrival.arrival_fat, card4_arrival.NTKfactor, 0.167, 95.7, 0.137, 95.7, 101.15, 1.02);
                    card4_arrival.ntk25_1 = card4_arrival.ntk30_2;


                    card4_arrival.textView_25_1.setText(new DecimalFormat("###.##").format(card4_arrival.ntk25_1));
                    card4_arrival.textView_60_1.setText(new DecimalFormat("###.##").format(card4_arrival.ntk60_1));
                    card4_arrival.textView_60_2.setText(new DecimalFormat("###.##").format(card4_arrival.ntk60_2));
                    card4_arrival.textView_30_1.setText(new DecimalFormat("###.##").format(card4_arrival.ntk30_1));
                    card4_arrival.textView_30_2.setText(new DecimalFormat("###.##").format(card4_arrival.ntk30_2));
                    card4_arrival.textView_NTKfactor.setText(new DecimalFormat("###.##").format(card4_arrival.NTKfactor));
                    card4_arrival.arrival_alt = Textview_to_integer(card4_arrival.arrival_altitude_TextView.getText().toString());
                    card4_arrival.arrival_fat = Textview_to_integer(card4_arrival.arrival_fat_TextView.getText().toString());
                    card4_arrival.arrival_wind = Textview_to_integer(card4_arrival.arrival_wind_TextView.getText().toString());
                }

        }
    }

    private void checkstate(final cardarrival_ViewHolder card4_arrival) {
        if (card4_arrival.textView_caption_arrivalcard.isEnabled()) {


            if (card4_arrival.expandframe.getVisibility() == View.GONE) {
                card4_arrival.expandframe.setVisibility(View.VISIBLE);

                final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                card4_arrival.expandframe.measure(widthSpec, heightSpec);

                ValueAnimator mAnimator = slideAnimator3(0, card4_arrival.expandframe.getMeasuredHeight(), card4_arrival);
                mAnimator.start();
            } else {
                int finalHeight = card4_arrival.expandframe.getHeight();

                ValueAnimator mAnimator = slideAnimator3(finalHeight, 0, card4_arrival);

                mAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        //Height=0, but it set visibility to GONE
                        card4_arrival.expandframe.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                });
                mAnimator.start();

            }
        }
    }

    private void checkstate(final cardcruise_ViewHolder card3_cruise) {
        if (card3_cruise.textView_caption_cruisecard.isEnabled()) {


            if (card3_cruise.expandframe.getVisibility() == View.GONE) {
                card3_cruise.expandframe.setVisibility(View.VISIBLE);

                final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                card3_cruise.expandframe.measure(widthSpec, heightSpec);

                ValueAnimator mAnimator = slideAnimator2(0, card3_cruise.expandframe.getMeasuredHeight(), card3_cruise);
                mAnimator.start();
            } else {
                int finalHeight = card3_cruise.expandframe.getHeight();

                ValueAnimator mAnimator = slideAnimator2(finalHeight, 0, card3_cruise);

                mAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        //Height=0, but it set visibility to GONE
                        card3_cruise.expandframe.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                });
                mAnimator.start();

            }
        }
    }

    private void checkstate(final cardstart_ViewHolder card2_start) {
        if (card2_start.textView_caption_startcard.isEnabled()) {
            if (card2_start.expandframe.getVisibility() == View.GONE) {
                card2_start.expandframe.setVisibility(View.VISIBLE);

                final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                card2_start.expandframe.measure(widthSpec, heightSpec);

                ValueAnimator mAnimator = slideAnimator4(0, card2_start.expandframe.getMeasuredHeight(), card2_start);
                mAnimator.start();
            } else {
                int finalHeight = card2_start.expandframe.getHeight();

                ValueAnimator mAnimator = slideAnimator4(finalHeight, 0, card2_start);

                mAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        //Height=0, but it set visibility to GONE
                        card2_start.expandframe.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                });
                mAnimator.start();

            }
        }
    }

    private void checkstate(final cardtakeoff_ViewHolder card1_takeoff) {
        if (card1_takeoff.expandframe1.getVisibility() == View.GONE) {
            card1_takeoff.expandframe1.setVisibility(View.VISIBLE);

            final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            card1_takeoff.expandframe1.measure(widthSpec, heightSpec);

            ValueAnimator mAnimator = slideAnimator1(0, card1_takeoff.expandframe1.getMeasuredHeight(), card1_takeoff);
            mAnimator.start();
        } else {
            int finalHeight = card1_takeoff.expandframe1.getHeight();

            ValueAnimator mAnimator = slideAnimator1(finalHeight, 0, card1_takeoff);

            mAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //Height=0, but it set visibility to GONE
                    card1_takeoff.expandframe1.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

            });
            mAnimator.start();

        }
    }

    private ValueAnimator slideAnimator1(int start, int end, final cardtakeoff_ViewHolder card2) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = card2.expandframe1.getLayoutParams();
                layoutParams.height = value;
                card2.expandframe1.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private ValueAnimator slideAnimator2(int start, int end, final cardcruise_ViewHolder card2) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = card2.expandframe.getLayoutParams();
                layoutParams.height = value;
                card2.expandframe.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private ValueAnimator slideAnimator3(int start, int end, final cardarrival_ViewHolder card3) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = card3.expandframe.getLayoutParams();
                layoutParams.height = value;
                card3.expandframe.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    private ValueAnimator slideAnimator4(int start, int end, final cardstart_ViewHolder card) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = card.expandframe.getLayoutParams();
                layoutParams.height = value;
                card.expandframe.setLayoutParams(layoutParams);
            }
        });
        return animator;
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

    private class cardtakeoff_ViewHolder extends ViewHolder {
        FrameLayout expandframe1;
        FrameLayout expandframe2;
        View view;
        Integer takeoff_alt;
        Integer takeoff_alt_meter = 0;
        Integer takeoff_fat;
        Integer takeoff_wind;
        Integer takeoff_wind_msec;
        Integer OGETable = 0;
        Integer IGEtable = 0;
        Integer OGEWind = 0;
        Integer IGEWind = 0;
        Integer IgeTotal = 0;
        Integer OgeTotal = 0;
        double ntk60_1 = 0;
        double ntk60_2 = 0;
        double ntk30_1 = 0;
        double ntk30_2 = 0;
        double ntk25_1 = 0;
        double NTKfactor;


        TextView
                LoadforIGE_TextView,
                LoadforOGE_TextView,
                textView_OGEHOVERCAPABILITY,
                textView_IGEHOVERCAPABILITY,
                Takeoff_altitude_TextView,
                Takeoff_fat_TextView,
                Takeoff_wind_TextView,

        textView_link,
                textViewIGEtable,
                textViewOGEtable,
                textViewIgeTotal,
                textViewOGETotal,
                textViewOGEWind,
                textViewIGEWind,
                textView_25_1,
                textView_60_1,
                textView_60_2,
                textView_30_1,
                textView_30_2,
                textView_caption_takeoffcard;

        CheckBox Takeoff_Checkbox_useMeter, Takeoff_Checkbox_useMSec;
        Spinner Takeoff_Spinner_Wind_Direction;
        TextInputLayout textInputLayout_takeoff_alt, textInputLayout_takeoff_wind;
        private boolean usemeter;
        private boolean usemsec;
        private int level = 0;


        public cardtakeoff_ViewHolder(View v) {
            super(v);
            this.view = v;

            expandframe1 = v.findViewById(R.id.framelayout1);
            expandframe2 = v.findViewById(R.id.framelayout2);

            textView_caption_takeoffcard = v.findViewById(R.id.textView_caption);

            Takeoff_altitude_TextView = v.findViewById(R.id.Altitude_text);
            Takeoff_fat_TextView = v.findViewById(R.id.FATtext);
            Takeoff_wind_TextView = v.findViewById(R.id.windtext);
            Takeoff_Checkbox_useMeter = v.findViewById(R.id.checkBox_usemeter);
            Takeoff_Checkbox_useMSec = v.findViewById(R.id.checkBox_mtsec);
            Takeoff_Spinner_Wind_Direction = v.findViewById(R.id.spinner_wind_direction);

            LoadforIGE_TextView = v.findViewById(R.id.textViewLoadforIGE);
            LoadforOGE_TextView = v.findViewById(R.id.textViewLoadforOGE);

            textViewIGEtable = v.findViewById(R.id.textViewIGEtable);
            textViewOGEtable = v.findViewById(R.id.textviewOGEtable);
            textViewIgeTotal = v.findViewById(R.id.textViewIgeTotal);
            textViewOGETotal = v.findViewById(R.id.textViewOGETotal);
            textViewOGEWind = v.findViewById(R.id.textViewOGEWing);
            textViewIGEWind = v.findViewById(R.id.textViewIGEWind);

            textView_25_1 = v.findViewById(R.id.textView_25_1);
            textView_60_1 = v.findViewById(R.id.textView_60_1);
            textView_60_2 = v.findViewById(R.id.textView_60_2);
            textView_30_1 = v.findViewById(R.id.textView_30_1);
            textView_30_2 = v.findViewById(R.id.textView_30_2);


            textView_OGEHOVERCAPABILITY = v.findViewById(R.id.textView_OGEHOVERCAPABILITY);
            textView_IGEHOVERCAPABILITY = v.findViewById(R.id.textView_IGEHOVERCAPABILITY);


            textInputLayout_takeoff_alt = v.findViewById(R.id.textInputLayout_Altitude_text);
            textInputLayout_takeoff_wind = v.findViewById(R.id.textInputLayout_Wind);
            Takeoff_Spinner_Wind_Direction = v.findViewById(R.id.spinner_wind_direction);

            takeoff_alt = Textview_to_integer(Takeoff_altitude_TextView.getText().toString());
            takeoff_fat = Textview_to_integer(Takeoff_fat_TextView.getText().toString());
            takeoff_wind = Textview_to_integer(Takeoff_wind_TextView.getText().toString());


        }
    }

    private class cardcruise_ViewHolder extends ViewHolder {
        FrameLayout expandframe;

        TextView Cruise_altitude_TextView,
                Cruise_fat_TextView,
                textView_cruise1_1,
                textView_cruise1_2,
                textView_cruise2_1,
                textView_cruise2_2,
                textView_NTKfactor,
                textView_singleeng25,
                textView_vmax,
                textView_vmin,
                textView_fuel,
                textView_caption_cruisecard,
                textView_singleeng30;


        CheckBox Cruise_Checkbox_useMeter;
        TextInputLayout textInputLayout_Cruise_alt;
        Integer Cruise_fat;
        double cruise1_1;
        double cruise1_2;
        double cruise2_1;
        double cruise2_2;
        double NTKfactor;
        private Integer Cruise_alt;
        private Integer Cruise_alt_meter;
        private boolean usemeter;


        public cardcruise_ViewHolder(View v) {
            super(v);
            expandframe = v.findViewById(R.id.cruise_expandframe);
            Cruise_altitude_TextView = v.findViewById(R.id.Altitude_text);
            Cruise_fat_TextView = v.findViewById(R.id.FATtext);
            Cruise_Checkbox_useMeter = v.findViewById(R.id.checkBox_usemeter);
            textInputLayout_Cruise_alt = v.findViewById(R.id.textInputLayout_Altitude_text);
            Cruise_alt = Textview_to_integer(Cruise_altitude_TextView.getText().toString());
            Cruise_fat = Textview_to_integer(Cruise_fat_TextView.getText().toString());

            textView_cruise1_1 = v.findViewById(R.id.textView_cruise1_1);
            textView_cruise1_2 = v.findViewById(R.id.textView_cruise1_2);
            textView_cruise2_1 = v.findViewById(R.id.textView_cruise2_1);
            textView_cruise2_2 = v.findViewById(R.id.textView_cruise2_2);
            textView_NTKfactor = v.findViewById(R.id.textView_NTKfactor);
            textView_caption_cruisecard = v.findViewById(R.id.textView_caption_cruisecard);


            textView_singleeng25 = v.findViewById(R.id.textView_singleeng25);
            textView_vmax = v.findViewById(R.id.textView_vmax);
            textView_vmin = v.findViewById(R.id.textView_vmin);
            textView_fuel = v.findViewById(R.id.textView_fuel);
            textView_singleeng30 = v.findViewById(R.id.textView_singleeng30);


        }
    }

    private class cardarrival_ViewHolder extends ViewHolder {
        public Integer OGEWind;
        public Integer IGEWind;
        public int IgeTotal;
        public int OgeTotal;
        public Integer IGEtable = 0;
        public Integer OGETable = 0;
        public int landing_GW = 0;
        FrameLayout expandframe;
        Integer arrival_alt;
        Integer arrival_alt_meter = 0;
        Integer arrival_fat;
        Integer arrival_wind;
        Integer arrival_wind_msec;
        double ntk25_1;
        double ntk60_2;
        double ntk60_1;
        double ntk30_1;
        double ntk30_2;
        double NTKfactor;

        TextView
                textView_25_1,
                textView_60_1,
                textView_60_2,
                textView_30_1,
                textView_30_2,
                arrival_altitude_TextView,
                arrival_fat_TextView,
                arrival_wind_TextView,
                textView_NTKfactor,
                textViewOGEWind,
                textViewIGEWind,
                textViewIgeTotal,
                textView_landing_GW,
                textViewOGETotal,
                textViewIGEtable,
                textViewFlighttime,
                Takeoff_OGEtable,
                textView_ftimemin,
                textView_caption_arrivalcard;

        CheckBox arrival_Checkbox_useMeter, arrival_Checkbox_useMSec;
        Spinner arrival_Spinner_Wind_Direction;
        TextInputLayout textInputLayout_arrival_alt, textInputLayout_arrival_wind, textInputLayout_flight_time;
        private boolean usemeter;
        private boolean usemsec;

        public cardarrival_ViewHolder(View v) {
            super(v);

            arrival_altitude_TextView = v.findViewById(R.id.Altitude_text);
            arrival_fat_TextView = v.findViewById(R.id.FATtext);
            arrival_wind_TextView = v.findViewById(R.id.windtext);
            arrival_Checkbox_useMeter = v.findViewById(R.id.checkBox_usemeter);
            arrival_Checkbox_useMSec = v.findViewById(R.id.checkBox_mtsec);
            arrival_Spinner_Wind_Direction = v.findViewById(R.id.spinner_wind_direction);
            textView_25_1 = v.findViewById(R.id.textView_25_1);
            textView_60_1 = v.findViewById(R.id.textView_60_1);
            textView_60_2 = v.findViewById(R.id.textView_60_2);
            textView_30_1 = v.findViewById(R.id.textView_30_1);
            textView_30_2 = v.findViewById(R.id.textView_30_2);
            textView_NTKfactor = v.findViewById(R.id.textView_NTKfactor);
            textViewIGEtable = v.findViewById(R.id.textViewIGEtable);
            Takeoff_OGEtable = v.findViewById(R.id.textViewOGETotal);
            textViewFlighttime = v.findViewById(R.id.textViewFlighttime);
            textView_ftimemin = v.findViewById(R.id.textView_ftimemin);
            textView_caption_arrivalcard = v.findViewById(R.id.textView_caption_arrivalcard);


            textInputLayout_arrival_alt = v.findViewById(R.id.textInputLayout_Altitude_text);
            textInputLayout_arrival_wind = v.findViewById(R.id.textInputLayout_Wind);
            arrival_Spinner_Wind_Direction = v.findViewById(R.id.spinner_wind_direction);
            textInputLayout_flight_time = v.findViewById(R.id.textInputLayout_flight_time);

            textViewOGEWind = v.findViewById(R.id.textViewOGEWing);
            textViewIGEWind = v.findViewById(R.id.textViewIGEWind);
            textViewIgeTotal = v.findViewById(R.id.textViewIgeTotal);
            textViewOGETotal = v.findViewById(R.id.textViewOGETotal);
            textView_landing_GW = v.findViewById(R.id.textView_landing_GW);


        }
    }

    private class cardstart_ViewHolder extends ViewHolder {
        FrameLayout expandframe;
        TextView textView_apuairpress;
        TextView textView_maxtit;
        TextView textView_idlentk1;
        TextView textView_idlentk2;
        TextView textView_acctit;
        TextView textView_ntkfactor;
        TextView textView_caption_startcard;
        double apuairpress;
        double maxtit;
        double idlentk_1;
        double idlentk_2;
        double acctit;
        double ntkfactor;

        public cardstart_ViewHolder(View v) {
            super(v);
            expandframe = v.findViewById(R.id.start_expandframe);
            textView_apuairpress = v.findViewById(R.id.textView_apiairpress);
            textView_maxtit = v.findViewById(R.id.textView_maxtit);
            textView_idlentk1 = v.findViewById(R.id.textView_idlentk1);
            textView_idlentk2 = v.findViewById(R.id.textView_idlentk2);
            textView_acctit = v.findViewById(R.id.textView_acctit);
            textView_ntkfactor = v.findViewById(R.id.textView_ntkfactor);
            textView_caption_startcard = v.findViewById(R.id.textView_caption_startcard);


        }
    }
}
