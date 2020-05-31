package com.ddcorp.mi_17helicopterperformance;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;

class fragment_starts_recyler extends RecyclerView.Adapter<fragment_starts_recyler.ViewHolder> {
    public FragmentManager fragmentManager;
    public Integer dustprotect = 0, hoist = 0, hirss = 0, antiice = 0, internaltank = 0, bambibucket = 0, armour = 0;
    DatabaseAccess databaseAccess;
    Integer operating = 0, crew, fuel, load = 0, GW = 0;


    Context context;
    Integer altitude_meter = 0, fat = 0;
    Integer wind;
    Integer wind_meter;
    int measuredHeight;

    private int card_0 = 0;
    private int card_1 = 1;
    environmental_condition_listener environmental_condition_listener;
    private boolean noentry=true;

    public interface environmental_condition_listener{
        public void environmental_condition_listener(int altitude ,int fat,int wind_kt,int  wind_direction,boolean usemeter,boolean usemsec);
    }

    public fragment_starts_recyler(Context context,environmental_condition_listener environmental_condition_listener) {
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
    public fragment_starts_recyler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        if (viewType == card_0) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_start_card0, parent, false);

            return new cardstart_card0(v);
        }


        if (viewType == card_1) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_start_card1, parent, false);

            return new cardstart_card1(v);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull fragment_starts_recyler.ViewHolder holder, int position) {
        if (holder.getItemViewType() == card_0) {
            final cardstart_card0 card = (cardstart_card0) holder;


            if (card.usemeter) {
                card.altitude_meter = Textview_to_integer(card.altitude_TextView.getText().toString());
                card.altitude = (int) (Math.round(Double.valueOf(card.altitude_meter) * 3.2808399));
            } else {
                card.altitude = Textview_to_integer(card.altitude_TextView.getText().toString());
                card.altitude_meter = (int) (Math.round(Double.valueOf(card.altitude) * 0.3048));
            }

            if (card.usemsec) {
                wind_meter = Textview_to_integer(card.wind_TextView.getText().toString());
                wind = (int) (Math.round(Double.valueOf(wind_meter) * 1.94384449412));
            } else {
                wind = Textview_to_integer(card.wind_TextView.getText().toString());
                wind_meter = (int) (Math.round(Double.valueOf(wind) * 0.514444444));
            }

            fat = Textview_to_integer(card.fat_TextView.getText().toString());
            altitude_meter = card.altitude_meter;



            //  wind = Textview_to_integer(Takeoff_wind_TextView.getText().toString());


            card.Takeoff_Spinner_Wind_Direction.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            card.Checkbox_useMeter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (card.Checkbox_useMeter.isChecked()) {
                        card.usemeter = true;
                        card.textInputLayout_takeoff_alt.setHint("Altitude (mt.)");
                        if (!card.altitude_TextView.getText().toString().isEmpty() ) {
                            altitude_meter = (int) (Math.round(Double.valueOf(altitude_meter) * 0.3048));
                            card.altitude_TextView.setText(card.altitude_meter.toString());
                        }
                    } else {
                        card.usemeter = false;
                        card.textInputLayout_takeoff_alt.setHint("Altitude (Ft.)");
                        if (!card.altitude_TextView.getText().toString().isEmpty() ) {
                            altitude_meter = (int) (Math.round(Double.valueOf(altitude_meter) * 3.2808399));
                            card.altitude_TextView.setText(card.altitude.toString());
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            card.Checkbox_useMSec.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (card.Checkbox_useMSec.isChecked()) {
                        card.usemsec=true;
                        //wind = (int) (Math.round(Double.valueOf(wind) * 0.514444444));
                        card.textInputLayout_wind.setHint("Wind (m/sec.)");
                        if (!card.wind_TextView.getText().toString().isEmpty() && wind > 0) {
                      //      wind_meter = (int) (Math.round(Double.valueOf(wind) * 0.514444444));
                            card.wind_TextView.setText(wind_meter.toString());
                        }

                    } else {
                        card.usemsec=false;
                        // wind = (int) (Math.round(Double.valueOf(wind) * 1.94384449412));
                        card.textInputLayout_wind.setHint("Wind (Kt.)");
                        if (!card.wind_TextView.getText().toString().isEmpty() && wind_meter > 0) {
                     //       wind = (int) (Math.round(Double.valueOf(wind_meter) * 1.94384449412));
                            card.wind_TextView.setText(wind.toString());
                        }
                    }
                    notifyDataSetChanged();

                }
            });

            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!card.altitude_TextView.getText().toString().isEmpty() && !card.fat_TextView.getText().toString().isEmpty()) {
                        noentry=false;
                    }else noentry=true;

                    notifyDataSetChanged();
                    hidesoftkeyboard(v);
                }
            };
            card.altitude_TextView.setOnClickListener(onClickListener);
            card.fat_TextView.setOnClickListener(onClickListener);
            card.wind_TextView.setOnClickListener(onClickListener);
            if (card.CheckBox_sameas.isChecked()) {
                int alt;
                int sendedwind;
                if (card.usemeter) {
                    alt=card.altitude_meter;
                }else alt=card.altitude;
                if (card.usemsec) {
                    sendedwind = wind_meter;
                }else sendedwind=wind;
                environmental_condition_listener.environmental_condition_listener(alt,fat,sendedwind,card.Spinner_Wind_Direction.getSelectedItemPosition(),card.usemeter,card.usemsec);
            }
            card.CheckBox_sameas.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    notifyDataSetChanged();
                }
            });
        }
        if (holder.getItemViewType() == card_1) {
            final cardstart_card1 card2_start = (cardstart_card1) holder;
            if (!noentry) {
                card2_start.textView_caption_startcard.setEnabled(true);

                //=((altitude_meter*0,0046)*-1)+(1,97-((fat^1,007)/6200))
                card2_start.apuairpress = ((fat * 0.0046) * -1) + (1.97 - (Math.pow(altitude_meter, 1.007) / 6200));
                card2_start.apuairpress = databaseAccess.get_apu_press(altitude_meter,fat);
                if (fat <= 13) {
                    card2_start.maxtit = (fat * 2.5) + 750;
                } else {
                    card2_start.maxtit = 780;
                }
                //=EĞER(S5<=6;((S5-5)*0,192)+72;((S5-5)*0,0254)+72)
                if (fat <= 6) {
                    card2_start.idlentk_1 = ((fat - 5) * 0.192) + 72;
                } else {
                    card2_start.idlentk_1 = ((fat - 5) * 0.0254) + 72;
                }

                card2_start.idlentk_2 = card2_start.idlentk_1 + 6;

                //=EĞER(S5<=38;((S5*4,1)+785);985
                if (fat <= 38) {
                    card2_start.acctit = (fat * 4.1) + 785;
                } else {
                    card2_start.acctit = 985;
                }

                //=(N5/1000)*1,3
                card2_start.ntkfactor = (Double.valueOf(altitude_meter) / 1000) * 1.3;


                card2_start.textView_apuairpress.setText(new DecimalFormat("#.##").format(card2_start.apuairpress));
                card2_start.textView_maxtit.setText(new DecimalFormat("###").format(card2_start.maxtit));
                card2_start.textView_idlentk1.setText(new DecimalFormat("##.##").format(card2_start.idlentk_1));
                card2_start.textView_idlentk2.setText(new DecimalFormat("##.##").format(card2_start.idlentk_2));
                card2_start.textView_acctit.setText(new DecimalFormat("###").format(card2_start.acctit));
                card2_start.textView_ntkfactor.setText(new DecimalFormat("#.##").format(card2_start.ntkfactor));
            } else {
                card2_start.textView_caption_startcard.setEnabled(false);

            }


        }

    }



 /*   private double interpolator(double x,double x0, double x1,double y0,double y1) {

        return y0+(y1-y0)*(x-x0)/(x1-x0);
    }
*/

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
        return 2;
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

    private class cardstart_card1 extends ViewHolder {

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

        public cardstart_card1(View v) {
            super(v);

            textView_apuairpress = v.findViewById(R.id.textView_apiairpress);
            textView_maxtit = v.findViewById(R.id.textView_maxtit);
            textView_idlentk1 = v.findViewById(R.id.textView_idlentk1);
            textView_idlentk2 = v.findViewById(R.id.textView_idlentk2);
            textView_acctit = v.findViewById(R.id.textView_acctit);
            textView_ntkfactor = v.findViewById(R.id.textView_ntkfactor);
            textView_caption_startcard = v.findViewById(R.id.textView_caption_startcard);


        }
    }

    private class cardstart_card0 extends ViewHolder {

        TextView altitude_TextView;
        TextView fat_TextView;
        TextView wind_TextView;
        CheckBox Checkbox_useMeter;
        CheckBox Checkbox_useMSec;
        CheckBox CheckBox_sameas;
        Spinner Spinner_Wind_Direction;
        Spinner Takeoff_Spinner_Wind_Direction;
        TextInputLayout textInputLayout_takeoff_alt, textInputLayout_wind;
        Integer altitude;
        Integer altitude_meter = 0;


        private boolean usemeter;
        private boolean usemsec;

        public cardstart_card0(View v) {
            super(v);

            altitude_TextView = v.findViewById(R.id.Altitude_text);
            fat_TextView = v.findViewById(R.id.FATtext);
            wind_TextView = v.findViewById(R.id.windtext);
            Checkbox_useMeter = v.findViewById(R.id.checkBox_usemeter);
            Checkbox_useMSec = v.findViewById(R.id.checkBox_mtsec);
            CheckBox_sameas = v.findViewById(R.id.checkBox_sameas);
            Spinner_Wind_Direction = v.findViewById(R.id.spinner_wind_direction);

            textInputLayout_takeoff_alt = v.findViewById(R.id.textInputLayout_Altitude_text);
            textInputLayout_wind = v.findViewById(R.id.textInputLayout_Wind);
            Takeoff_Spinner_Wind_Direction = v.findViewById(R.id.spinner_wind_direction);

            fragment_starts_recyler.this.altitude_meter = Textview_to_integer(altitude_TextView.getText().toString());
            fragment_starts_recyler.this.fat = Textview_to_integer(fat_TextView.getText().toString());
            wind = Textview_to_integer(wind_TextView.getText().toString());


        }
    }
}
